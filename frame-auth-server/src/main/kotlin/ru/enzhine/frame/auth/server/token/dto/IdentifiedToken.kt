package ru.enzhine.frame.auth.server.token.dto

data class IdentifiedToken(
    val id: Long,
    val content: String
)
