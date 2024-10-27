package ru.enzhine.frame.auth.server.repo.dto

import java.time.OffsetDateTime

data class UserProfileEntity(
    val username: String,
    val email: String,
    val userAuthId: Long,
    val updatedAt: OffsetDateTime?,
)
