package ru.enzhine.frame.auth.api.dto

import jakarta.validation.constraints.Size

class ChangePasswordRequest (
    @field:Size(min=1)
    val newPassword: String
)
