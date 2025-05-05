package ru.kotlix.frame.auth.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import ru.kotlix.frame.auth.api.AccessToken
import ru.kotlix.frame.auth.api.dto.BasicLoginRequest
import ru.kotlix.frame.auth.api.dto.BasicRegisterRequest
import ru.kotlix.frame.auth.api.dto.ChangeEmailRequest
import ru.kotlix.frame.auth.api.dto.ChangePasswordRequest
import ru.kotlix.frame.auth.api.dto.ChangeUsernameRequest

@FeignClient(name = "frame-auth-client", path = "/api/v1")
interface AuthClient {
    @GetMapping("/auth/login")
    fun basicLogin(
        @RequestBody request: BasicLoginRequest,
    ): AccessToken

    @PostMapping("/auth/register")
    fun basicRegister(
        @RequestBody request: BasicRegisterRequest,
    )

    @GetMapping("/auth/register-verify/{secret}")
    fun verifyRegister(
        @PathVariable("secret") secret: String,
    )

    @PostMapping("/auth/check/{token}")
    fun checkAuth(
        @PathVariable("token") token: AccessToken,
    )

    @PostMapping("/profile/email")
    fun changeEmail(
        @RequestHeader("Authorization") bearerToken: AccessToken,
        @RequestBody request: ChangeEmailRequest,
    )

    @GetMapping("/profile/email-verify/{secret}")
    fun changeEmailApply(
        @RequestHeader("Authorization") bearerToken: AccessToken,
        @PathVariable("secret") secret: String,
    )

    @PostMapping("/profile/username")
    fun changeUsername(
        @RequestHeader("Authorization") bearerToken: AccessToken,
        @RequestBody request: ChangeUsernameRequest,
    )

    @GetMapping("/profile/username-verify/{secret}")
    fun changeUsernameApply(
        @RequestHeader("Authorization") bearerToken: AccessToken,
        @PathVariable("secret") secret: String,
    )

    @PostMapping("/profile/password")
    fun changePassword(
        @RequestHeader("Authorization") bearerToken: AccessToken,
        @RequestBody request: ChangePasswordRequest,
    )

    @GetMapping("/profile/password-verify/{secret}")
    fun changePasswordApply(
        @RequestHeader("Authorization") bearerToken: AccessToken,
        @PathVariable("secret") secret: String,
    )
}
