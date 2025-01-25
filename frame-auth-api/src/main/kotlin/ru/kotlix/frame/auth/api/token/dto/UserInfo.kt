package ru.kotlix.frame.auth.api.token.dto

data class UserInfo(
    val id: Long,
    val login: String,
    val username: String,
    val timestamp: Long,
)
