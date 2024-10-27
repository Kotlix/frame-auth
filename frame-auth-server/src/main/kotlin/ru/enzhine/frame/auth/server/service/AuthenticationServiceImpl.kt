package ru.enzhine.frame.auth.server.service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import ru.enzhine.frame.auth.api.AccessToken
import ru.enzhine.frame.auth.server.coding.PasswordEncryptor
import ru.enzhine.frame.auth.server.repo.ConfirmVerifyRepository
import ru.enzhine.frame.auth.server.repo.TokenRepository
import ru.enzhine.frame.auth.server.repo.UserAuthRepository
import ru.enzhine.frame.auth.server.repo.UserProfileRepository
import ru.enzhine.frame.auth.server.repo.dto.ConfirmVerifyEntity
import ru.enzhine.frame.auth.server.repo.dto.TokenEntity
import ru.enzhine.frame.auth.server.repo.dto.UserAuthEntity
import ru.enzhine.frame.auth.server.repo.dto.UserProfileEntity
import ru.enzhine.frame.auth.server.service.exception.AuthenticationAlreadyExistsException
import ru.enzhine.frame.auth.server.service.exception.AuthenticationExpiredException
import ru.enzhine.frame.auth.server.service.exception.AuthenticationFailedException
import ru.enzhine.frame.auth.server.service.exception.RegistrationFailedException
import ru.enzhine.frame.auth.server.token.TokenCoder
import ru.enzhine.frame.auth.server.token.TokenGenerator
import ru.enzhine.frame.auth.server.token.dto.IdentifiedToken
import java.time.OffsetDateTime

@Service
class AuthenticationServiceImpl(
    val userAuthRepository: UserAuthRepository,
    val userProfileRepository: UserProfileRepository,
    val tokenRepository: TokenRepository,
    val confirmVerifyRepository: ConfirmVerifyRepository,
    val passwordEncryptor: PasswordEncryptor,
    val tokenCoder: TokenCoder,
    val tokenGenerator: TokenGenerator,
    val javaMailSender: JavaMailSender
) : AuthenticationService {

    @Transactional(
        readOnly = false,
        propagation = Propagation.REQUIRED
    )
    override fun basicLogin(login: String, password: String): AccessToken {
        val auth = userAuthRepository.findByLogin(login) ?: throw AuthenticationFailedException("Unknown login")

        if (!auth.verified) {
            throw AuthenticationFailedException("Not verified auth")
        }
        if (auth.banned!!) {
            throw AuthenticationFailedException("Auth is banned")
        }

        val encrypted = passwordEncryptor.encrypt(password)
        if (auth.password != encrypted) {
            throw AuthenticationFailedException("Wrong password")
        }

        val authId = auth.id!!
        val token = tokenCoder.stringifyIdentifiedToken(tokenGenerator.generateIdentifiedToken(authId, login))
        tokenRepository.upsertToken(TokenEntity(token, OffsetDateTime.now().plusMinutes(5), authId))
        return token
    }

    @Transactional(
        readOnly = false,
        propagation = Propagation.REQUIRED
    )
    override fun basicRegister(login: String, password: String, username: String, email: String) {
        userAuthRepository.findByLogin(login)?.let { auth ->
            if (auth.verified) {
                throw RegistrationFailedException("Login already taken")
            }
            confirmVerifyRepository.findById(auth.id!!)?.let {
                if (it.expiresAt.isBefore(OffsetDateTime.now())) {
                    confirmVerifyRepository.remove(it)
                }else {
                    throw RegistrationFailedException("Login possibly taken")
                }
            }
            userProfileRepository.findByAuthId(auth.id)?.let {
                userProfileRepository.remove(it)
            }
            userAuthRepository.remove(auth)
        }
        userProfileRepository.findByEmail(email)?.let { profile ->
            val auth = userAuthRepository.findById(profile.userAuthId)!!
           if (auth.verified) {
               throw RegistrationFailedException("Email is in use")
           }
            confirmVerifyRepository.findById(auth.id!!)?.let {
                if (it.expiresAt.isBefore(OffsetDateTime.now())) {
                    confirmVerifyRepository.remove(it)
                }else {
                    throw RegistrationFailedException("Email possibly is in use")
                }
            }
            userProfileRepository.findByAuthId(auth.id)?.let {
                userProfileRepository.remove(it)
            }
            userAuthRepository.remove(auth)
        }

        val authEnt = userAuthRepository.save(UserAuthEntity(
            id = null,
            login = login,
            password = passwordEncryptor.encrypt(password),
            banned = null,
            verified = false,
            createdAt = null,
            updatedAt = null
        ))
        val profileEnt = userProfileRepository.save(UserProfileEntity(
            username = username,
            email = email,
            userAuthId = authEnt.id!!,
            updatedAt = null
        ))

        val now = OffsetDateTime.now()
        val inToken = cropToken(tokenGenerator.generateRandomToken(now.toString()), 32)
        val confReg = confirmVerifyRepository.save(ConfirmVerifyEntity(
            id = null,
            secret = inToken,
            createdAt = null,
            expiresAt = now.plusMinutes(15),
            confirmed = null,
            userId = authEnt.id
        ))
        val outToken = tokenCoder.stringifyIdentifiedToken(IdentifiedToken(confReg.id!!, inToken))

        val mimeMessage = javaMailSender.createMimeMessage()
        MimeMessageHelper(mimeMessage, false).apply {
            setFrom("registration@frame.enzhine.dev")
            setTo(profileEnt.email)
            setSubject("Please, verify your registration")
            setText("Your verification token is: '$outToken'. Unless you verify your email in 15 minutes its bound account will be removed.")
        }
        javaMailSender.send(mimeMessage)
    }

    private fun cropToken(token: String, len: Int): String {
        return if (token.length < len) {
            token
        }else {
            token.substring(0, len)
        }
    }

    @Transactional(
        readOnly = false,
        propagation = Propagation.REQUIRED
    )
    override fun verifyRegister(secret: String) {
        val token = tokenCoder.parseIdentifiedToken(secret)
        val confirmVerifyEntity = confirmVerifyRepository.findById(token.id)
            ?: throw AuthenticationFailedException("Verification token id is unknown")

        if (confirmVerifyEntity.confirmed!!) {
            throw AuthenticationAlreadyExistsException("Already confirmed")
        }
        if (confirmVerifyEntity.secret != token.content) {
            throw AuthenticationFailedException("Wrong token")
        }
        if (confirmVerifyEntity.expiresAt.isBefore(OffsetDateTime.now())) {
            throw AuthenticationExpiredException("Token expired")
        }
        val conf = confirmVerifyRepository.setConfirmed(confirmVerifyEntity)
        userAuthRepository.setVerified(userAuthRepository.findById(conf.userId)!!)
    }

    @Transactional(
        readOnly = true,
        propagation = Propagation.REQUIRED
    )
    override fun validateToken(token: AccessToken) {
        val tw = tokenCoder.parseIdentifiedToken(token)
        val te = tokenRepository.findTokenByAuthId(tw.id)
            ?: throw AuthenticationFailedException("Token authId unknown")
        if (te.expiresAt.isBefore(OffsetDateTime.now())) {
            throw AuthenticationExpiredException("Token expired")
        }
        if (te.content != token) {
            throw AuthenticationFailedException("Token wrong")
        }
    }
}