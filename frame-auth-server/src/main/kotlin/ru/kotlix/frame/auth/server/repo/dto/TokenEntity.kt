package ru.kotlix.frame.auth.server.repo.dto

import java.time.OffsetDateTime

data class TokenEntity(
    val content: String,
    val expiresAt: OffsetDateTime,
    val userAuthId: Long,
)
