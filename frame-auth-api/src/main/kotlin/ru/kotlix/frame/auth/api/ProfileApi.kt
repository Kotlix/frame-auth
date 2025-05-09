package ru.kotlix.frame.auth.api

import ru.kotlix.frame.auth.api.dto.ChangeEmailRequest
import ru.kotlix.frame.auth.api.dto.ChangePasswordRequest
import ru.kotlix.frame.auth.api.dto.ChangeUsernameRequest
import ru.kotlix.frame.auth.api.dto.FullProfileInfoDto
import ru.kotlix.frame.auth.api.dto.ProfileInfoDto

interface ProfileApi {
    fun changeEmail(
        initiatorId: Long,
        request: ChangeEmailRequest,
    )

    fun changeEmailApply(
        initiatorId: Long,
        secret: String,
    )

    fun changeUsername(
        initiatorId: Long,
        request: ChangeUsernameRequest,
    )

    fun changeUsernameApply(
        initiatorId: Long,
        secret: String,
    )

    fun changePassword(
        initiatorId: Long,
        request: ChangePasswordRequest,
    )

    fun changePasswordApply(
        initiatorId: Long,
        secret: String,
    )

    fun getMyProfileInfo(initiatorId: Long): FullProfileInfoDto

    fun getProfileInfo(
        initiatorId: Long,
        userId: Long,
    ): ProfileInfoDto
}
