package ru.kotlix.frame.auth.server.service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import ru.kotlix.frame.auth.api.token.TokenDecoder
import ru.kotlix.frame.auth.api.token.TokenEncoder
import ru.kotlix.frame.auth.server.coding.PasswordEncryptor
import ru.kotlix.frame.auth.server.repo.ConfirmEmailRepository
import ru.kotlix.frame.auth.server.repo.ConfirmPasswordRepository
import ru.kotlix.frame.auth.server.repo.ConfirmUsernameRepository
import ru.kotlix.frame.auth.server.repo.UserAuthRepository
import ru.kotlix.frame.auth.server.repo.UserProfileRepository
import ru.kotlix.frame.auth.server.repo.dto.ConfirmEmailEntity
import ru.kotlix.frame.auth.server.repo.dto.ConfirmPasswordEntity
import ru.kotlix.frame.auth.server.repo.dto.ConfirmUsernameEntity
import ru.kotlix.frame.auth.server.service.dto.ServiceUser
import ru.kotlix.frame.auth.server.service.exception.AuthenticationFailedException
import ru.kotlix.frame.auth.server.service.exception.ProfileChangeException
import ru.kotlix.frame.auth.server.token.dto.VerificationToken
import java.time.Instant
import java.time.OffsetDateTime
import java.util.UUID

@Service
class ProfileServiceImpl(
    val confirmEmailRepository: ConfirmEmailRepository,
    val confirmPasswordRepository: ConfirmPasswordRepository,
    val confirmUsernameRepository: ConfirmUsernameRepository,
    val authRepository: UserAuthRepository,
    val profileRepository: UserProfileRepository,
    val tokenEncoder: TokenEncoder<VerificationToken>,
    val tokenDecoder: TokenDecoder<VerificationToken>,
    val passwordEncryptor: PasswordEncryptor,
    val javaMailSender: JavaMailSender,
) : ProfileService {
    @Transactional(
        readOnly = false,
        propagation = Propagation.REQUIRED,
    )
    override fun changeEmail(
        serviceUser: ServiceUser,
        newEmail: String,
    ) {
        confirmEmailRepository.findLast(serviceUser.id)?.let {
            if (it.createdAt!!.plusMinutes(15).isAfter(OffsetDateTime.now())) {
                throw ProfileChangeException("Retry delay required")
            }
        }
        val now = OffsetDateTime.now()
        val secret = cropToken(UUID.randomUUID().toString())
        val confEmail =
            confirmEmailRepository.save(
                ConfirmEmailEntity(
                    id = null,
                    secret = secret,
                    createdAt = null,
                    expiresAt = now.plusMinutes(15),
                    confirmed = null,
                    userId = serviceUser.id,
                    newEmail = newEmail,
                ),
            )
        val token =
            tokenEncoder.encodeAndSign(
                VerificationToken(
                    id = confEmail.id!!,
                    content = secret,
                    timestamp = Instant.now().epochSecond,
                ),
            )

        val mimeMessage = javaMailSender.createMimeMessage()
        MimeMessageHelper(mimeMessage, false).apply {
            setFrom("registration@frame.kotlix.dev")
            setTo(serviceUser.email)
            setSubject("Please, confirm your email update")
            setText(
                "You are changing your current email to $newEmail. Your verification token is: '$token'." +
                    "Unless you confirm your email update in 15 minutes it will be cancelled.",
            )
        }
        javaMailSender.send(mimeMessage)
    }

    @Transactional(
        readOnly = false,
        propagation = Propagation.REQUIRED,
    )
    override fun verifyEmail(token: String) {
        val verificationToken = tokenDecoder.getPayload(token)
        val confirmEmailEntity =
            confirmEmailRepository.findById(verificationToken.id)
                ?: throw AuthenticationFailedException("Confirmation token id is unknown")

        if (confirmEmailEntity.confirmed!!) {
            throw AuthenticationFailedException("Already confirmed")
        }
        if (confirmEmailEntity.secret != verificationToken.content) {
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
        propagation = Propagation.REQUIRED,
    )
    override fun changeUsername(
        serviceUser: ServiceUser,
        newUsername: String,
    ) {
        confirmUsernameRepository.findLast(serviceUser.id)?.let {
            if (it.createdAt!!.plusMinutes(15).isAfter(OffsetDateTime.now())) {
                throw ProfileChangeException("Retry delay required")
            }
        }
        val now = OffsetDateTime.now()
        val secret = cropToken(UUID.randomUUID().toString())
        val confUsername =
            confirmUsernameRepository.save(
                ConfirmUsernameEntity(
                    id = null,
                    secret = secret,
                    createdAt = null,
                    expiresAt = now.plusMinutes(15),
                    confirmed = null,
                    userId = serviceUser.id,
                    newUsername = newUsername,
                ),
            )
        val outToken =
            tokenEncoder.encodeAndSign(
                VerificationToken(
                    id = confUsername.id!!,
                    content = secret,
                    timestamp = Instant.now().epochSecond,
                ),
            )

        val mimeMessage = javaMailSender.createMimeMessage()
        MimeMessageHelper(mimeMessage, false).apply {
            setFrom("registration@frame.kotlix.dev")
            setTo(serviceUser.email)
            setSubject("Please, confirm your username update")
            setText(
                "You are changing your current username to $newUsername. Your verification token is: '$outToken'." +
                    "Unless you confirm your username update in 15 minutes it will be cancelled.",
            )
        }
        javaMailSender.send(mimeMessage)
    }

    @Transactional(
        readOnly = false,
        propagation = Propagation.REQUIRED,
    )
    override fun verifyUsername(token: String) {
        val verificationToken = tokenDecoder.getPayload(token)
        val confirmUsernameEntity =
            confirmUsernameRepository.findById(verificationToken.id)
                ?: throw AuthenticationFailedException("Confirmation token id is unknown")

        if (confirmUsernameEntity.confirmed!!) {
            throw AuthenticationFailedException("Already confirmed")
        }
        if (confirmUsernameEntity.secret != verificationToken.content) {
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
        propagation = Propagation.REQUIRED,
    )
    override fun changePassword(
        serviceUser: ServiceUser,
        newPassword: String,
    ) {
        confirmPasswordRepository.findLast(serviceUser.id)?.let {
            if (it.createdAt!!.plusMinutes(15).isAfter(OffsetDateTime.now())) {
                throw ProfileChangeException("Retry delay required")
            }
        }
        val now = OffsetDateTime.now()
        val secret = cropToken(UUID.randomUUID().toString())
        val confPassword =
            confirmPasswordRepository.save(
                ConfirmPasswordEntity(
                    id = null,
                    secret = secret,
                    createdAt = null,
                    expiresAt = now.plusMinutes(15),
                    confirmed = null,
                    userId = serviceUser.id,
                    newPassword = passwordEncryptor.encrypt(newPassword),
                ),
            )
        val outToken =
            tokenEncoder.encodeAndSign(
                VerificationToken(
                    id = confPassword.id!!,
                    content = secret,
                    timestamp = Instant.now().epochSecond,
                ),
            )

        val mimeMessage = javaMailSender.createMimeMessage()
        MimeMessageHelper(mimeMessage, false).apply {
            setFrom("registration@frame.kotlix.dev")
            setTo(serviceUser.email)
            setSubject("Please, confirm your password update")
            setText(
                "You are changing your current password. Your verification token is: '$outToken'." +
                    "Unless you confirm your password update in 15 minutes it will be cancelled.",
            )
        }
        javaMailSender.send(mimeMessage)
    }

    @Transactional(
        readOnly = false,
        propagation = Propagation.REQUIRED,
    )
    override fun verifyPassword(token: String) {
        val verificationToken = tokenDecoder.getPayload(token)
        val confirmPasswordEntity =
            confirmPasswordRepository.findById(verificationToken.id)
                ?: throw AuthenticationFailedException("Confirmation token id is unknown")

        if (confirmPasswordEntity.confirmed!!) {
            throw AuthenticationFailedException("Already confirmed")
        }
        if (confirmPasswordEntity.secret != verificationToken.content) {
            throw AuthenticationFailedException("Wrong token")
        }
        if (confirmPasswordEntity.expiresAt.isBefore(OffsetDateTime.now())) {
            throw AuthenticationFailedException("Token expired")
        }
        confirmPasswordRepository.setConfirmed(confirmPasswordEntity)
        authRepository.updatePassword(confirmPasswordEntity.userId, confirmPasswordEntity.newPassword)
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
