package ru.enzhine.frame.auth.api.dto

import jakarta.validation.constraints.Email

data class ChangeEmailRequest(
    @field:Email
    val newEmail: String
)
