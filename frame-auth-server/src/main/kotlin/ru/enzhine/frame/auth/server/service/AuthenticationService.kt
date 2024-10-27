package ru.enzhine.frame.auth.server.service

import ru.enzhine.frame.auth.api.AccessToken

interface AuthenticationService {

    fun basicLogin(login: String, password: String): AccessToken

    fun basicRegister(login: String, password: String, username: String, email: String)

    fun verifyRegister(secret: String)

    fun validateToken(token: AccessToken)
}