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
import ru.kotlix.frame.auth.api.dto.FullProfileInfoDto
import ru.kotlix.frame.auth.api.dto.ProfileInfoDto

@FeignClient(name = "frame-auth-client", path = "/api/v1")
interface AuthClient {
    @PostMapping("/auth/login")
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
        @RequestHeader("Initiator-Id")
        initiatorId: Long,
        @RequestBody request: ChangeEmailRequest,
    )

    @GetMapping("/profile/email-verify/{secret}")
    fun changeEmailApply(
        @RequestHeader("Initiator-Id")
        initiatorId: Long,
        @PathVariable("secret") secret: String,
    )

    @PostMapping("/profile/username")
    fun changeUsername(
        @RequestHeader("Initiator-Id")
        initiatorId: Long,
        @RequestBody request: ChangeUsernameRequest,
    )

    @GetMapping("/profile/username-verify/{secret}")
    fun changeUsernameApply(
        @RequestHeader("Initiator-Id")
        initiatorId: Long,
        @PathVariable("secret") secret: String,
    )

    @PostMapping("/profile/password")
    fun changePassword(
        @RequestHeader("Initiator-Id")
        initiatorId: Long,
        @RequestBody request: ChangePasswordRequest,
    )

    @GetMapping("/profile/password-verify/{secret}")
    fun changePasswordApply(
        @RequestHeader("Initiator-Id")
        initiatorId: Long,
        @PathVariable("secret") secret: String,
    )

    @GetMapping("/profile/getInfo")
    fun getMyProfileInfo(
        @RequestHeader("Initiator-Id") initiatorId: Long,
    ): FullProfileInfoDto

    @GetMapping("/profile/getInfo/{userId}")
    fun getProfileInfo(
        @RequestHeader("Initiator-Id") initiatorId: Long,
        @PathVariable("userId") userId: Long,
    ): ProfileInfoDto
}
