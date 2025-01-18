package ru.kotlix.frame.auth.server.handler

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.kotlix.frame.auth.server.controller.Anchor
import ru.kotlix.frame.auth.server.service.exception.AuthenticationExpiredException
import ru.kotlix.frame.auth.server.service.exception.AuthenticationFailedException
import ru.kotlix.frame.auth.server.service.exception.ProfileChangeException
import ru.kotlix.frame.auth.server.service.exception.RegistrationFailedException

@RestControllerAdvice(basePackageClasses = [Anchor::class])
class AuthControllerExceptionHandler {
    @ExceptionHandler(
        value = [
            AuthenticationExpiredException::class,
            AuthenticationFailedException::class,
        ],
    )
    fun conflictExceptionHandler() = ResponseEntity<Void>(HttpStatus.BAD_REQUEST)

    @ExceptionHandler(
        value = [
            ru.kotlix.frame.auth.server.service.exception.AuthenticationAlreadyExistsException::class,
            RegistrationFailedException::class,
            ProfileChangeException::class,
        ],
    )
    fun badRequestExceptionHandler() = ResponseEntity<Void>(HttpStatus.CONFLICT)
}
