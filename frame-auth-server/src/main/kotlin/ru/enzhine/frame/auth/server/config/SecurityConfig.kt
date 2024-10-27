package ru.enzhine.frame.auth.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import ru.enzhine.frame.auth.server.security.TokenAuthenticationFilter
import ru.enzhine.frame.auth.server.security.TokenAuthenticationProvider
import ru.enzhine.frame.auth.server.service.AuthenticationService

@Configuration
class SecurityConfig {

    @Bean
    fun authenticationProvider(authenticationService: AuthenticationService): AuthenticationProvider =
        TokenAuthenticationProvider(authenticationService)

    @Bean
    fun authenticationManager(authenticationProvider: AuthenticationProvider): AuthenticationManager =
        ProviderManager(authenticationProvider)

    @Bean
    fun filter(authenticationManager: AuthenticationManager) =
        TokenAuthenticationFilter(AntPathRequestMatcher("/api/v1/profile/**"), authenticationManager)

    @Bean
    fun filterChain(
        http: HttpSecurity,
        authenticationProvider: TokenAuthenticationProvider,
        authenticationFilter: TokenAuthenticationFilter
        ): SecurityFilterChain = http
            .cors {it.disable() }
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .logout { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(authenticationFilter, AnonymousAuthenticationFilter::class.java).authorizeHttpRequests { it
                .requestMatchers("/api/v1/profile/**").authenticated()
            }
            .authorizeHttpRequests { it
                .requestMatchers("/api/v1/auth/**").permitAll()
            }
            .build()
}