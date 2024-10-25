package ru.enzhine.frame.auth.api.dto

import jakarta.validation.constraints.Size

data class ChangeUsernameRequest(
    @field:Size(min=1, max=32)
    val newUsername: String
)
