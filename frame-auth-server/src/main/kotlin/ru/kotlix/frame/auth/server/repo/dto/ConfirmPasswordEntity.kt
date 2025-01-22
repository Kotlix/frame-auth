package ru.kotlix.frame.auth.server.repo.dto

import java.time.OffsetDateTime

data class ConfirmPasswordEntity(
    val id: Long?,
    val secret: String,
    val createdAt: OffsetDateTime?,
    val expiresAt: OffsetDateTime,
    val confirmed: Boolean?,
    val userId: Long,
    val newPassword: String,
)
