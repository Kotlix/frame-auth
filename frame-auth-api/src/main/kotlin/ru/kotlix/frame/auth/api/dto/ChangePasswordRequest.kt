package ru.kotlix.frame.auth.api.dto

import jakarta.validation.constraints.Size

data class ChangePasswordRequest(
    @field:Size(min = 1)
    val newPassword: String,
)
