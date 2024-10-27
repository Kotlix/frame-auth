package ru.enzhine.frame.auth.server.repo.dto

import java.time.OffsetDateTime

data class UserAuthEntity(
    val id: Long?,
    val login: String,
    val password: String,
    val banned: Boolean?,
    val verified: Boolean,
    val createdAt: OffsetDateTime?,
    val updatedAt: OffsetDateTime?,
)
