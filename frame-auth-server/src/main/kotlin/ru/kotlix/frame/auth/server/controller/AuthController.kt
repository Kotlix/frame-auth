package ru.kotlix.frame.auth.server.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.kotlix.frame.auth.api.AccessToken
import ru.kotlix.frame.auth.api.AuthApi
import ru.kotlix.frame.auth.api.dto.BasicLoginRequest
import ru.kotlix.frame.auth.api.dto.BasicRegisterRequest
import ru.kotlix.frame.auth.server.service.AuthenticationService

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    val authenticationService: AuthenticationService,
) : AuthApi {
    @GetMapping("/login")
    override fun basicLogin(
        @RequestBody request: BasicLoginRequest,
    ): AccessToken {
        return authenticationService.basicLogin(
            login = request.login,
            password = request.password,
        )
    }

    @PostMapping("/register")
    override fun basicRegister(
        @RequestBody request: BasicRegisterRequest,
    ) {
        authenticationService.basicRegister(
            login = request.login,
            password = request.password,
            email = request.email,
            username = request.username,
        )
    }

    @GetMapping("/register-verify/{secret}")
    override fun verifyRegister(
        @PathVariable("secret") secret: String,
    ) {
        authenticationService.verifyRegister(secret)
    }

    @PostMapping("/check")
    override fun checkAuth(
        @RequestBody token: AccessToken,
    ) {
        authenticationService.serviceUserByToken(token)
    }
}
