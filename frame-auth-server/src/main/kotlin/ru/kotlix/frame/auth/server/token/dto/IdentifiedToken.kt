package ru.kotlix.frame.auth.server.token.dto

data class IdentifiedToken(
    val id: Long,
    val content: String,
)
