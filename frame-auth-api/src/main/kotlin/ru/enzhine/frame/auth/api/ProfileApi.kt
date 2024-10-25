package ru.enzhine.frame.auth.api

import ru.enzhine.frame.auth.api.dto.ChangeEmailRequest
import ru.enzhine.frame.auth.api.dto.ChangePasswordRequest
import ru.enzhine.frame.auth.api.dto.ChangeUsernameRequest

interface ProfileApi {
    fun changeEmail(request: ChangeEmailRequest)

    fun changeEmailApply(secret: String)

    fun changeUsername(request: ChangeUsernameRequest)

    fun changeUsernameApply(secret: String)

    fun changePassword(request: ChangePasswordRequest)

    fun changePasswordApply(secret: String)
}