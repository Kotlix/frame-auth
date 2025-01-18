package ru.kotlix.frame.auth.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import ru.kotlix.frame.auth.api.AccessToken
import ru.kotlix.frame.auth.api.AuthApi
import ru.kotlix.frame.auth.api.dto.BasicLoginRequest
import ru.kotlix.frame.auth.api.dto.BasicRegisterRequest

@FeignClient(name = "frame-auth-client")
@RequestMapping("/api/v1/auth")
interface AuthClient : AuthApi {
    @GetMapping("/login")
    override fun basicLogin(
        @RequestBody request: BasicLoginRequest,
    ): AccessToken

    @PostMapping("/register")
    override fun basicRegister(
        @RequestBody request: BasicRegisterRequest,
    )

    @GetMapping("/register-verify/{secret}")
    override fun verifyRegister(
        @PathVariable("secret") secret: String,
    )

    @PostMapping("/check")
    override fun checkAuth(
        @RequestBody token: AccessToken,
    )
}
