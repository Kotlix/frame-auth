package ru.kotlix.frame.auth.server.service

import ru.kotlix.frame.auth.server.service.dto.ServiceUser

interface ProfileService {
    fun changeEmail(
        serviceUser: ServiceUser,
        newEmail: String,
    )

    fun verifyEmail(secret: String)

    fun changeUsername(
        serviceUser: ServiceUser,
        newUsername: String,
    )

    fun verifyUsername(secret: String)

    fun changePassword(
        serviceUser: ServiceUser,
        newPassword: String,
    )

    fun verifyPassword(secret: String)
}
