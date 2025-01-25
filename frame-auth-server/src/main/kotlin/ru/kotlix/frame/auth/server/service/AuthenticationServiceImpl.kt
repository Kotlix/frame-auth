package ru.kotlix.frame.auth.server.service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import ru.kotlix.frame.auth.api.AccessToken
import ru.kotlix.frame.auth.api.token.TokenDecoder
import ru.kotlix.frame.auth.api.token.TokenEncoder
import ru.kotlix.frame.auth.api.token.dto.UserInfo
import ru.kotlix.frame.auth.server.coding.PasswordEncryptor
import ru.kotlix.frame.auth.server.repo.ConfirmVerifyRepository
import ru.kotlix.frame.auth.server.repo.TokenRepository
import ru.kotlix.frame.auth.server.repo.UserAuthRepository
import ru.kotlix.frame.auth.server.repo.UserProfileRepository
import ru.kotlix.frame.auth.server.repo.dto.ConfirmVerifyEntity
import ru.kotlix.frame.auth.server.repo.dto.TokenEntity
import ru.kotlix.frame.auth.server.repo.dto.UserAuthEntity
import ru.kotlix.frame.auth.server.repo.dto.UserProfileEntity
import ru.kotlix.frame.auth.server.service.dto.ServiceUser
import ru.kotlix.frame.auth.server.service.exception.AuthenticationExpiredException
import ru.kotlix.frame.auth.server.service.exception.AuthenticationFailedException
import ru.kotlix.frame.auth.server.service.exception.RegistrationFailedException
import ru.kotlix.frame.auth.server.token.dto.VerificationToken
import java.time.Instant
import java.time.OffsetDateTime
import java.util.UUID

@Service
class AuthenticationServiceImpl(
    val userAuthRepository: UserAuthRepository,
    val userProfileRepository: UserProfileRepository,
    val tokenRepository: TokenRepository,
    val confirmVerifyRepository: ConfirmVerifyRepository,
    val passwordEncryptor: PasswordEncryptor,
    val authTokenEncoder: TokenEncoder<UserInfo>,
    val authTokenDecoder: TokenDecoder<UserInfo>,
    val verifyTokenEncoder: TokenEncoder<VerificationToken>,
    val verifyTokenDecoder: TokenDecoder<VerificationToken>,
    val javaMailSender: JavaMailSender,
) : AuthenticationService {
    @Transactional(
        readOnly = false,
        propagation = Propagation.REQUIRED,
    )
    override fun basicLogin(
        login: String,
        password: String,
    ): AccessToken {
        val authEnt = userAuthRepository.findByLogin(login) ?: throw AuthenticationFailedException("Unknown login")

        if (!authEnt.verified) {
            throw AuthenticationFailedException("Not verified auth")
        }
        if (authEnt.banned!!) {
            throw AuthenticationFailedException("Auth is banned")
        }

        val encrypted = passwordEncryptor.encrypt(password)
        if (authEnt.password != encrypted) {
            throw AuthenticationFailedException("Wrong password")
        }

        val authId = authEnt.id!!
        val profileEnt = userProfileRepository.findByAuthId(authId)!!
        val username = profileEnt.username
        val token =
            authTokenEncoder.encodeAndSign(
                UserInfo(
                    id = authId,
                    login = login,
                    username = username,
                    timestamp = Instant.now().epochSecond,
                ),
            )
        tokenRepository.upsertToken(TokenEntity(token, OffsetDateTime.now().plusMinutes(5), authId))
        return token
    }

    @Transactional(
        readOnly = false,
        propagation = Propagation.REQUIRED,
    )
    override fun basicRegister(
        login: String,
        password: String,
        username: String,
        email: String,
    ) {
        userAuthRepository.findByLogin(login)?.let { auth ->
            if (auth.verified) {
                throw RegistrationFailedException("Login already taken")
            }
            confirmVerifyRepository.findById(auth.id!!)?.let {
                if (it.expiresAt.isBefore(OffsetDateTime.now())) {
                    confirmVerifyRepository.remove(it)
                } else {
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
                } else {
                    throw RegistrationFailedException("Email possibly is in use")
                }
            }
            userProfileRepository.findByAuthId(auth.id)?.let {
                userProfileRepository.remove(it)
            }
            userAuthRepository.remove(auth)
        }

        val authEnt =
            userAuthRepository.save(
                UserAuthEntity(
                    id = null,
                    login = login,
                    password = passwordEncryptor.encrypt(password),
                    banned = null,
                    verified = false,
                    createdAt = null,
                    updatedAt = null,
                ),
            )
        val profileEnt =
            userProfileRepository.save(
                UserProfileEntity(
                    username = username,
                    email = email,
                    userAuthId = authEnt.id!!,
                    updatedAt = null,
                ),
            )

        val now = OffsetDateTime.now()
        val secret = cropToken(UUID.randomUUID().toString())
        val confEntity =
            confirmVerifyRepository.save(
                ConfirmVerifyEntity(
                    id = null,
                    secret = secret,
                    createdAt = null,
                    expiresAt = now.plusMinutes(15),
                    confirmed = null,
                    userId = authEnt.id,
                ),
            )
        val token =
            verifyTokenEncoder.encodeAndSign(
                VerificationToken(
                    id = confEntity.id!!,
                    content = secret,
                    timestamp = Instant.now().epochSecond,
                ),
            )

        val mimeMessage = javaMailSender.createMimeMessage()
        MimeMessageHelper(mimeMessage, false).apply {
            setFrom("registration@frame.kotlix.dev")
            setTo(profileEnt.email)
            setSubject("Please, verify your registration")
            setText(
                "Your verification token is: '$token'. Unless you verify your email in 15 minutes its bound account will be removed.",
            )
        }
        javaMailSender.send(mimeMessage)
    }

    @Transactional(
        readOnly = false,
        propagation = Propagation.REQUIRED,
    )
    override fun verifyRegister(secret: String) {
        val verificationToken = verifyTokenDecoder.getPayload(secret)
        val confirmVerifyEntity =
            confirmVerifyRepository.findById(verificationToken.id)
                ?: throw AuthenticationFailedException("Confirmation token id is unknown")

        if (confirmVerifyEntity.confirmed!!) {
            throw ru.kotlix.frame.auth.server.service.exception.AuthenticationAlreadyExistsException("Already confirmed")
        }
        if (confirmVerifyEntity.secret != verificationToken.content) {
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
        propagation = Propagation.REQUIRED,
    )
    override fun serviceUserByToken(token: AccessToken): ServiceUser {
        val tokenPayload = authTokenDecoder.getPayload(token)
        val te =
            tokenRepository.findTokenByAuthId(tokenPayload.id)
                ?: throw AuthenticationFailedException("Token authId unknown")
        if (te.expiresAt.isBefore(OffsetDateTime.now())) {
            throw AuthenticationExpiredException("Token expired")
        }
        if (authTokenEncoder.encodeAndSign(tokenPayload) != token) {
            throw AuthenticationFailedException("Token is wrong")
        }

        val user = userAuthRepository.findById(te.userAuthId)!!
        val profile = userProfileRepository.findByAuthId(te.userAuthId)!!
        return ServiceUser(
            id = user.id!!,
            login = user.login,
            username = profile.username,
            email = profile.email,
        )
    }

    private fun cropToken(token: String): String {
        val len = 32
        return if (token.length < len) {
            token
        } else {
            token.substring(0, len)
        }
    }
}
