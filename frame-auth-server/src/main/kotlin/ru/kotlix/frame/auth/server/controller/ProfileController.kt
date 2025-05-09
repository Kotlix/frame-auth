package ru.kotlix.frame.auth.server.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.kotlix.frame.auth.api.ProfileApi
import ru.kotlix.frame.auth.api.dto.ChangeEmailRequest
import ru.kotlix.frame.auth.api.dto.ChangePasswordRequest
import ru.kotlix.frame.auth.api.dto.ChangeUsernameRequest
import ru.kotlix.frame.auth.api.dto.FullProfileInfoDto
import ru.kotlix.frame.auth.api.dto.ProfileInfoDto
import ru.kotlix.frame.auth.server.mapper.toFullProfileInfoDto
import ru.kotlix.frame.auth.server.mapper.toProfileInfoDto
import ru.kotlix.frame.auth.server.service.ProfileService

@RestController
@RequestMapping("/api/v1/profile")
class ProfileController(
    val profileService: ProfileService,
) : ProfileApi {
    @PostMapping("/email")
    override fun changeEmail(
        @RequestHeader("Initiator-Id")
        initiatorId: Long,
        @RequestBody request: ChangeEmailRequest,
    ) {
        profileService.changeEmail(initiatorId, request.newEmail)
    }

    @GetMapping("/email-verify/{secret}")
    override fun changeEmailApply(
        @RequestHeader("Initiator-Id")
        initiatorId: Long,
        @PathVariable("secret") secret: String,
    ) {
        profileService.verifyEmail(initiatorId, secret)
    }

    @PostMapping("/username")
    override fun changeUsername(
        @RequestHeader("Initiator-Id")
        initiatorId: Long,
        @RequestBody request: ChangeUsernameRequest,
    ) {
        profileService.changeUsername(initiatorId, request.newUsername)
    }

    @GetMapping("/username-verify/{secret}")
    override fun changeUsernameApply(
        @RequestHeader("Initiator-Id")
        initiatorId: Long,
        @PathVariable("secret") secret: String,
    ) {
        profileService.verifyUsername(initiatorId, secret)
    }

    @PostMapping("/password")
    override fun changePassword(
        @RequestHeader("Initiator-Id")
        initiatorId: Long,
        @RequestBody request: ChangePasswordRequest,
    ) {
        profileService.changePassword(initiatorId, request.newPassword)
    }

    @GetMapping("/password-verify/{secret}")
    override fun changePasswordApply(
        @RequestHeader("Initiator-Id")
        initiatorId: Long,
        @PathVariable("secret") secret: String,
    ) {
        profileService.verifyPassword(initiatorId, secret)
    }

    @GetMapping("/getInfo")
    override fun getMyProfileInfo(
        @RequestHeader("Initiator-Id") initiatorId: Long,
    ): FullProfileInfoDto = profileService.getInfoAboutUser(initiatorId).toFullProfileInfoDto()

    @GetMapping("/getInfo/{userId}")
    override fun getProfileInfo(
        @RequestHeader("Initiator-Id") initiatorId: Long,
        @PathVariable("userId") userId: Long,
    ): ProfileInfoDto = profileService.getInfoAboutOtherUser(initiatorId, userId).toProfileInfoDto()
}
