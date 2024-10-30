package ru.enzhine.frame.auth.server.security

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import ru.enzhine.frame.auth.server.service.AuthenticationService
import ru.enzhine.frame.auth.server.service.exception.AuthenticationException
import ru.enzhine.frame.auth.server.service.exception.AuthenticationFailedException

class TokenAuthenticationProvider(
    private val authenticationService: AuthenticationService
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication?): Authentication {
        val token = authentication?.principal as String? ?: throw AccessDeniedException("Authentication required")
        try {
            val user = authenticationService.serviceUserByToken(token)
            return PreAuthenticatedAuthenticationToken(user, "ROLE_USER").apply {
                isAuthenticated = true
            }
        } catch (ex: AuthenticationException) {
            throw AccessDeniedException(ex.message)
        }
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return authentication?.let { PreAuthenticatedAuthenticationToken::class.java.isAssignableFrom(it) } ?: false
    }
}