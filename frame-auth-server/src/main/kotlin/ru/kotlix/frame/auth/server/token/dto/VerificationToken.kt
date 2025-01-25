package ru.kotlix.frame.auth.server.token.dto

data class VerificationToken(
    val id: Long,
    val content: String,
    val timestamp: Long,
)
