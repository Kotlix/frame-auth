package ru.kotlix.frame.auth.server.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.web.util.matcher.RequestMatcher

class TokenAuthenticationFilter(
    requestMather: RequestMatcher,
    authenticationManager: AuthenticationManager,
) : AbstractAuthenticationProcessingFilter(
        requestMather,
        authenticationManager,
    ) {
    override fun attemptAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
    ): Authentication {
        val authentication =
            request?.getHeader("Authorization")
                ?: throw AuthenticationCredentialsNotFoundException("Token not provided!")

        if (!authentication.startsWith("Bearer ")) {
            throw AuthenticationCredentialsNotFoundException("Bearer authentication expected!")
        }
        val token = authentication.substring(7)

        return authenticationManager.authenticate(PreAuthenticatedAuthenticationToken(token, null))
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?,
    ) {
        SecurityContextHolder.getContext().authentication = authResult
        chain?.doFilter(request, response)
        // super.successfulAuthentication(request, response, chain, authResult)
    }
}
