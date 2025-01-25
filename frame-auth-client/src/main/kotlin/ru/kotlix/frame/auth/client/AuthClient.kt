package ru.kotlix.frame.auth.client

import feign.Headers
import feign.Param
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import ru.kotlix.frame.auth.api.AccessToken
import ru.kotlix.frame.auth.api.dto.BasicLoginRequest
import ru.kotlix.frame.auth.api.dto.BasicRegisterRequest
import ru.kotlix.frame.auth.api.dto.ChangeEmailRequest
import ru.kotlix.frame.auth.api.dto.ChangePasswordRequest
import ru.kotlix.frame.auth.api.dto.ChangeUsernameRequest

@FeignClient(name = "frame-auth-client")
@RequestMapping("/api/v1")
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

    @Headers("Authorization: Bearer {token}")
    @PostMapping("/profile/email")
    fun changeEmail(
        @Param("token") authToken: AccessToken,
        @RequestBody request: ChangeEmailRequest,
    )

    @Headers("Authorization: Bearer {token}")
    @GetMapping("/profile/email-verify/{secret}")
    fun changeEmailApply(
        @Param("token") authToken: AccessToken,
        @PathVariable("secret") secret: String,
    )

    @Headers("Authorization: Bearer {token}")
    @PostMapping("/profile/username")
    fun changeUsername(
        @Param("token") authToken: AccessToken,
        @RequestBody request: ChangeUsernameRequest,
    )

    @Headers("Authorization: Bearer {token}")
    @GetMapping("/profile/username-verify/{secret}")
    fun changeUsernameApply(
        @Param("token") authToken: AccessToken,
        @PathVariable("secret") secret: String,
    )

    @Headers("Authorization: Bearer {token}")
    @PostMapping("/profile/password")
    fun changePassword(
        @Param("token") authToken: AccessToken,
        @RequestBody request: ChangePasswordRequest,
    )

    @Headers("Authorization: Bearer {token}")
    @GetMapping("/profile/password-verify/{secret}")
    fun changePasswordApply(
        @Param("token") authToken: AccessToken,
        @PathVariable("secret") secret: String,
    )
}
