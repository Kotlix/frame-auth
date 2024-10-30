package ru.enzhine.frame.auth.server.handler

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.enzhine.frame.auth.server.controller.Anchor
import ru.enzhine.frame.auth.server.service.exception.*

@RestControllerAdvice(basePackageClasses = [Anchor::class])
class AuthControllerExceptionHandler {

    @ExceptionHandler(value = [
        AuthenticationExpiredException::class,
        AuthenticationFailedException::class
    ])
    fun conflictExceptionHandler() = ResponseEntity<Void>(HttpStatus.BAD_REQUEST)

    @ExceptionHandler(value = [
        AuthenticationAlreadyExistsException::class,
        RegistrationFailedException::class,
        ProfileChangeException::class
    ])
    fun badRequestExceptionHandler() = ResponseEntity<Void>(HttpStatus.CONFLICT)
}