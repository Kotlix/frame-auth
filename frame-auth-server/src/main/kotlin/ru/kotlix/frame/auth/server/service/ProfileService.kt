package ru.kotlix.frame.auth.server.service

import ru.kotlix.frame.auth.server.service.dto.DetailProfileInfo
import ru.kotlix.frame.auth.server.service.dto.ProfileInfo

interface ProfileService {
    fun changeEmail(
        initiatorId: Long,
        newEmail: String,
    )

    fun verifyEmail(
        initiatorId: Long,
        token: String,
    )

    fun changeUsername(
        initiatorId: Long,
        newUsername: String,
    )

    fun verifyUsername(
        initiatorId: Long,
        token: String,
    )

    fun changePassword(
        initiatorId: Long,
        newPassword: String,
    )

    fun verifyPassword(
        initiatorId: Long,
        token: String,
    )

    fun getInfoAboutUser(initiatorId: Long): DetailProfileInfo

    fun getInfoAboutOtherUser(
        initiatorId: Long,
        userId: Long,
    ): ProfileInfo
}
