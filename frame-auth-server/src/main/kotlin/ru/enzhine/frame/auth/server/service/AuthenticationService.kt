package ru.enzhine.frame.auth.server.service

import ru.enzhine.frame.auth.api.AccessToken
import ru.enzhine.frame.auth.server.service.dto.ServiceUser

interface AuthenticationService {

    fun basicLogin(login: String, password: String): AccessToken

    fun basicRegister(login: String, password: String, username: String, email: String)

    fun verifyRegister(secret: String)

    fun serviceUserByToken(token: AccessToken): ServiceUser
}