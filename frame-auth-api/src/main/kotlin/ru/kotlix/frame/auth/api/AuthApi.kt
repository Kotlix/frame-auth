package ru.kotlix.frame.auth.api

import ru.kotlix.frame.auth.api.dto.BasicLoginRequest
import ru.kotlix.frame.auth.api.dto.BasicRegisterRequest

typealias AccessToken = String

interface AuthApi {
    companion object {
        const val LOGIN_PATTERN = "^[\\w-]*$"
    }

    fun basicLogin(request: BasicLoginRequest): AccessToken

    fun basicRegister(request: BasicRegisterRequest)

    fun verifyRegister(secret: String)

    fun checkAuth(token: AccessToken)
}
