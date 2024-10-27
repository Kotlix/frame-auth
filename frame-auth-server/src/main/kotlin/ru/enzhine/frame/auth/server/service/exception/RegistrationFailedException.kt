package ru.enzhine.frame.auth.server.service.exception

class RegistrationFailedException(msg: String, base: Exception?)
    : RuntimeException(msg, base) {
    constructor(msg: String) : this(msg, null)
}