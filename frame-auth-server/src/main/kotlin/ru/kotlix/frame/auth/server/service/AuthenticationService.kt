package ru.kotlix.frame.auth.server.service

import ru.kotlix.frame.auth.api.AccessToken
import ru.kotlix.frame.auth.server.service.dto.ServiceUser

interface AuthenticationService {
    fun basicLogin(
        login: String,
        password: String,
    ): AccessToken

    fun basicRegister(
        login: String,
        password: String,
        username: String,
        email: String,
    )

    fun verifyRegister(secret: String)

    fun serviceUserByToken(token: AccessToken): ServiceUser
}
