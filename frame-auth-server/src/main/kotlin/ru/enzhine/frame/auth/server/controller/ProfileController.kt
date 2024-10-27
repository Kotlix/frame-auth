package ru.enzhine.frame.auth.server.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.enzhine.frame.auth.api.ProfileApi
import ru.enzhine.frame.auth.api.dto.*

@RestController
@RequestMapping("/api/v1/profile")
class ProfileController : ProfileApi {

    @GetMapping("/email")
    override fun changeEmail(@RequestBody request: ChangeEmailRequest) {
        TODO("Not yet implemented")
    }

    override fun changeEmailApply(secret: String) {
        TODO("Not yet implemented")
    }

    override fun changeUsername(request: ChangeUsernameRequest) {
        TODO("Not yet implemented")
    }

    override fun changeUsernameApply(secret: String) {
        TODO("Not yet implemented")
    }

    override fun changePassword(request: ChangePasswordRequest) {
        TODO("Not yet implemented")
    }

    override fun changePasswordApply(secret: String) {
        TODO("Not yet implemented")
    }

}