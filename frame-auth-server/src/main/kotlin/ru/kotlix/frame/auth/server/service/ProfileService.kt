package ru.kotlix.frame.auth.server.service

import ru.kotlix.frame.auth.server.service.dto.ServiceUser

interface ProfileService {
    fun changeEmail(
        serviceUser: ServiceUser,
        newEmail: String,
    )

    fun verifyEmail(token: String)

    fun changeUsername(
        serviceUser: ServiceUser,
        newUsername: String,
    )

    fun verifyUsername(token: String)

    fun changePassword(
        serviceUser: ServiceUser,
        newPassword: String,
    )

    fun verifyPassword(token: String)
}
