package ru.kotlix.frame.auth.server.service.exception

class AuthenticationExpiredException(msg: String, base: Exception?) :
    AuthenticationException(msg, base) {
    constructor(msg: String) : this(msg, null)
}
