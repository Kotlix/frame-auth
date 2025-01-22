package ru.kotlix.frame.auth.server.service.dto

data class ServiceUser(
    val id: Long,
    val login: String,
    val username: String,
    val email: String,
)
