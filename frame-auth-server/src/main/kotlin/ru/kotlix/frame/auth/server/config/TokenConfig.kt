package ru.kotlix.frame.auth.server.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.kotlix.frame.auth.api.token.TokenSigner
import ru.kotlix.frame.auth.token.JWSTokenDecoder
import ru.kotlix.frame.auth.token.JWSTokenEncoder

@Configuration
class TokenConfig {
    @Bean
    fun tokenDecoder(om: ObjectMapper) = JWSTokenDecoder(om)

    @Bean
    fun tokenEncoder(
        om: ObjectMapper,
        tokenSigner: TokenSigner,
    ) = JWSTokenEncoder(om, tokenSigner)
}
