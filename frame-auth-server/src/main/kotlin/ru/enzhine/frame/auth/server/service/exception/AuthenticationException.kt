package ru.enzhine.frame.auth.server.service.exception

open class AuthenticationException(msg: String, base: Exception?) : RuntimeException(msg, base)