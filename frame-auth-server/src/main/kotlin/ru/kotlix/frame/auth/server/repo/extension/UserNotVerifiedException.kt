package ru.kotlix.frame.auth.server.repo.extension

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotVerifiedException(id: Long) : RuntimeException("Auth entity id=$id is not verified.")