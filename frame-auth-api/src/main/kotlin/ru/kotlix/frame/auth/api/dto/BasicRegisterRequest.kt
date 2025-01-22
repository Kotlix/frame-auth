package ru.kotlix.frame.auth.api.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import ru.kotlix.frame.auth.api.AuthApi

data class BasicRegisterRequest(
    @field:Size(min = 1, max = 32)
    @field:Pattern(regexp = AuthApi.LOGIN_PATTERN)
    val login: String,
    @field:Size(min = 1)
    val password: String,
    @field:Size(min = 1, max = 32)
    val username: String,
    @field:Email
    val email: String,
)
