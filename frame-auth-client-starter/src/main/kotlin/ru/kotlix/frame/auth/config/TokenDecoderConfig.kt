package ru.kotlix.frame.auth.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.kotlix.frame.auth.token.JWSTokenDecoder

@Configuration
@ConditionalOnMissingBean(ru.kotlix.frame.auth.api.token.TokenDecoder::class)
class TokenDecoderConfig {
    @Bean
    fun decoder(om: ObjectMapper) = JWSTokenDecoder(om)
}
