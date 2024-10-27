package ru.enzhine.frame.auth.server.service.exception

class AuthenticationFailedException(msg: String, base: Exception?)
    : AuthenticationException(msg, base) {
    constructor(msg: String) : this(msg, null)
}