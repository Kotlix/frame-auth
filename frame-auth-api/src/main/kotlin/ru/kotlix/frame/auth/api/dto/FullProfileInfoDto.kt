package ru.kotlix.frame.auth.api.dto

data class FullProfileInfoDto(
    val id: Long,
    val login: String,
    val username: String,
    val email: String,
)
