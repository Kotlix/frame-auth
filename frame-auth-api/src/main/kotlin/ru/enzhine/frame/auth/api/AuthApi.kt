package ru.enzhine.frame.auth.api

import ru.enzhine.frame.auth.api.dto.BasicLoginRequest
import ru.enzhine.frame.auth.api.dto.BasicRegisterRequest

typealias AccessToken = String

interface AuthApi {
    companion object {
        const val LOGIN_PATTERN = "^[\\w-]*$"
    }

    fun basicLogin(request: BasicLoginRequest): AccessToken

    fun basicRegister(request: BasicRegisterRequest)

    fun checkAuth(token: AccessToken)
}