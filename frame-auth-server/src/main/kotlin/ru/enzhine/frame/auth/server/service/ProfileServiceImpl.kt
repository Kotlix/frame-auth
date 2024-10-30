package ru.enzhine.frame.auth.server.service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import ru.enzhine.frame.auth.server.repo.*
import ru.enzhine.frame.auth.server.repo.dto.ConfirmEmailEntity
import ru.enzhine.frame.auth.server.repo.dto.ConfirmPasswordEntity
import ru.enzhine.frame.auth.server.repo.dto.ConfirmUsernameEntity
import ru.enzhine.frame.auth.server.service.dto.ServiceUser
import ru.enzhine.frame.auth.server.service.exception.AuthenticationFailedException
import ru.enzhine.frame.auth.server.service.exception.ProfileChangeException
import ru.enzhine.frame.auth.server.token.TokenCoder
import ru.enzhine.frame.auth.server.token.TokenGenerator
import ru.enzhine.frame.auth.server.token.dto.IdentifiedToken
import java.time.OffsetDateTime

@Service
class ProfileServiceImpl(
    val confirmEmailRepository: ConfirmEmailRepository,
    val confirmPasswordRepository: ConfirmPasswordRepository,
    val confirmUsernameRepository: ConfirmUsernameRepository,
    val authRepository: UserAuthRepository,
    val profileRepository: UserProfileRepository,
    val tokenGenerator: TokenGenerator,
    val tokenCoder: TokenCoder,
    val javaMailSender: JavaMailSender
) : ProfileService {

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
    override fun changeEmail(serviceUser: ServiceUser, newEmail: String) {
        confirmEmailRepository.findLast(serviceUser.id)?.let {
            if (it.createdAt!!.plusMinutes(15).isAfter(OffsetDateTime.now())) {
                throw ProfileChangeException("Retry delay required")
            }
        }
        val now = OffsetDateTime.now()
        val inToken = cropToken(tokenGenerator.generateRandomToken(now.toString()), 32)
        val confEmail = confirmEmailRepository.save(ConfirmEmailEntity(
            id = null,
            secret = inToken,
            createdAt = null,
            expiresAt = now.plusMinutes(15),
            confirmed = null,
            userId = serviceUser.id,
            newEmail = newEmail
        ))
        val outToken = tokenCoder.stringifyIdentifiedToken(IdentifiedToken(confEmail.id!!, inToken))

        val mimeMessage = javaMailSender.createMimeMessage()
        MimeMessageHelper(mimeMessage, false).apply {
            setFrom("registration@frame.enzhine.dev")
            setTo(serviceUser.email)
            setSubject("Please, confirm your email update")
            setText("You are changing your current email to $newEmail. Your verification token is: '$outToken'. Unless you confirm your email update in 15 minutes it will be cancelled.")
        }
        javaMailSender.send(mimeMessage)
    }

    @Transactional(
        readOnly = false,
        propagation = Propagation.REQUIRED
    )
    override fun verifyEmail(secret: String) {
        val token = tokenCoder.parseIdentifiedToken(secret)
        val confirmEmailEntity = confirmEmailRepository.findById(token.id)
            ?: throw AuthenticationFailedException("Confirmation token id is unknown")

        if (confirmEmailEntity.confirmed!!) {
            throw AuthenticationFailedException("Already confirmed")
        }
        if (confirmEmailEntity.secret != token.content) {
            throw AuthenticationFailedException("Wrong token")
        }
        if (confirmEmailEntity.expiresAt.isBefore(OffsetDateTime.now())) {
            throw AuthenticationFailedException("Token expired")
        }
        confirmEmailRepository.setConfirmed(confirmEmailEntity)
        profileRepository.updateEmail(confirmEmailEntity.userId, confirmEmailEntity.newEmail)
    }

    @Transactional(
        readOnly = false,
        propagation = Propagation.REQUIRED
    )
    override fun changeUsername(serviceUser: ServiceUser, newUsername: String) {
        confirmUsernameRepository.findLast(serviceUser.id)?.let {
            if (it.createdAt!!.plusMinutes(15).isAfter(OffsetDateTime.now())) {
                throw ProfileChangeException("Retry delay required")
            }
        }
        val now = OffsetDateTime.now()
        val inToken = cropToken(tokenGenerator.generateRandomToken(now.toString()), 32)
        val confUsername = confirmUsernameRepository.save(ConfirmUsernameEntity(
            id = null,
            secret = inToken,
            createdAt = null,
            expiresAt = now.plusMinutes(15),
            confirmed = null,
            userId = serviceUser.id,
            newUsername = newUsername
        ))
        val outToken = tokenCoder.stringifyIdentifiedToken(IdentifiedToken(confUsername.id!!, inToken))

        val mimeMessage = javaMailSender.createMimeMessage()
        MimeMessageHelper(mimeMessage, false).apply {
            setFrom("registration@frame.enzhine.dev")
            setTo(serviceUser.email)
            setSubject("Please, confirm your username update")
            setText("You are changing your current username to $newUsername. Your verification token is: '$outToken'. Unless you confirm your username update in 15 minutes it will be cancelled.")
        }
        javaMailSender.send(mimeMessage)
    }

    @Transactional(
        readOnly = false,
        propagation = Propagation.REQUIRED
    )
    override fun verifyUsername(secret: String) {
        val token = tokenCoder.parseIdentifiedToken(secret)
        val confirmUsernameEntity = confirmUsernameRepository.findById(token.id)
            ?: throw AuthenticationFailedException("Confirmation token id is unknown")

        if (confirmUsernameEntity.confirmed!!) {
            throw AuthenticationFailedException("Already confirmed")
        }
        if (confirmUsernameEntity.secret != token.content) {
            throw AuthenticationFailedException("Wrong token")
        }
        if (confirmUsernameEntity.expiresAt.isBefore(OffsetDateTime.now())) {
            throw AuthenticationFailedException("Token expired")
        }
        confirmUsernameRepository.setConfirmed(confirmUsernameEntity)
        profileRepository.updateUsername(confirmUsernameEntity.userId, confirmUsernameEntity.newUsername)
    }

    @Transactional(
        readOnly = false,
        propagation = Propagation.REQUIRED
    )
    override fun changePassword(serviceUser: ServiceUser, newPassword: String) {
        confirmPasswordRepository.findLast(serviceUser.id)?.let {
            if (it.createdAt!!.plusMinutes(15).isAfter(OffsetDateTime.now())) {
                throw ProfileChangeException("Retry delay required")
            }
        }
        val now = OffsetDateTime.now()
        val inToken = cropToken(tokenGenerator.generateRandomToken(now.toString()), 32)
        val confPassword = confirmPasswordRepository.save(ConfirmPasswordEntity(
            id = null,
            secret = inToken,
            createdAt = null,
            expiresAt = now.plusMinutes(15),
            confirmed = null,
            userId = serviceUser.id,
            newPassword = newPassword
        ))
        val outToken = tokenCoder.stringifyIdentifiedToken(IdentifiedToken(confPassword.id!!, inToken))

        val mimeMessage = javaMailSender.createMimeMessage()
        MimeMessageHelper(mimeMessage, false).apply {
            setFrom("registration@frame.enzhine.dev")
            setTo(serviceUser.email)
            setSubject("Please, confirm your password update")
            setText("You are changing your current password. Your verification token is: '$outToken'. Unless you confirm your password update in 15 minutes it will be cancelled.")
        }
        javaMailSender.send(mimeMessage)
    }

    @Transactional(
        readOnly = false,
        propagation = Propagation.REQUIRED
    )
    override fun verifyPassword(secret: String) {
        val token = tokenCoder.parseIdentifiedToken(secret)
        val confirmPasswordEntity = confirmPasswordRepository.findById(token.id)
            ?: throw AuthenticationFailedException("Confirmation token id is unknown")

        if (confirmPasswordEntity.confirmed!!) {
            throw AuthenticationFailedException("Already confirmed")
        }
        if (confirmPasswordEntity.secret != token.content) {
            throw AuthenticationFailedException("Wrong token")
        }
        if (confirmPasswordEntity.expiresAt.isBefore(OffsetDateTime.now())) {
            throw AuthenticationFailedException("Token expired")
        }
        confirmPasswordRepository.setConfirmed(confirmPasswordEntity)
        authRepository.updatePassword(confirmPasswordEntity.userId, confirmPasswordEntity.newPassword)
    }
}