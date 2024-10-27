package ru.enzhine.frame.auth.server.repo.dto

import java.time.OffsetDateTime

data class ConfirmVerifyEntity(
    val id: Long?,
    val secret: String,
    val createdAt: OffsetDateTime?,
    val expiresAt: OffsetDateTime,
    val confirmed: Boolean?,
    val userId: Long,
)
