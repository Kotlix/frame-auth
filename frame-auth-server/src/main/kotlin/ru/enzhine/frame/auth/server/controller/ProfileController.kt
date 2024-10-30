package ru.enzhine.frame.auth.server.controller

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.enzhine.frame.auth.api.ProfileApi
import ru.enzhine.frame.auth.api.dto.*
import ru.enzhine.frame.auth.server.service.ProfileService
import ru.enzhine.frame.auth.server.service.dto.ServiceUser

@RestController
@RequestMapping("/api/v1/profile")
class ProfileController(
    val profileService: ProfileService
) : ProfileApi {

    @PostMapping("/email")
    override fun changeEmail(@RequestBody request: ChangeEmailRequest) {
        val serviceUser = SecurityContextHolder.getContext().authentication.principal as ServiceUser
        profileService.changeEmail(serviceUser, request.newEmail)
    }

    @GetMapping("/email-verify/{secret}")
    override fun changeEmailApply(@PathVariable("secret") secret: String) {
        profileService.verifyEmail(secret)
    }

    @PostMapping("/username")
    override fun changeUsername(@RequestBody request: ChangeUsernameRequest) {
        val serviceUser = SecurityContextHolder.getContext().authentication.principal as ServiceUser
        profileService.changeUsername(serviceUser, request.newUsername)
    }

    @GetMapping("/username-verify/{secret}")
    override fun changeUsernameApply(@PathVariable("secret") secret: String) {
        profileService.verifyUsername(secret)
    }

    @PostMapping("/password")
    override fun changePassword(@RequestBody request: ChangePasswordRequest) {
        val serviceUser = SecurityContextHolder.getContext().authentication.principal as ServiceUser
        profileService.changePassword(serviceUser, request.newPassword)
    }

    @GetMapping("/password-verify/{secret}")
    override fun changePasswordApply(@PathVariable("secret") secret: String) {
        profileService.verifyPassword(secret)
    }
}