package ru.kotlix.frame.auth.server.repo.extension

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ProfileNotFoundException(id: Long) : RuntimeException("Profile with id $id not found")
