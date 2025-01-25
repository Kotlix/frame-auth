package ru.kotlix.frame.auth.server.token

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import ru.kotlix.frame.auth.api.AccessToken
import ru.kotlix.frame.auth.api.token.TokenDecoder
import ru.kotlix.frame.auth.api.token.exception.TokenDecoderException
import ru.kotlix.frame.auth.server.coding.StatefulByteCoder
import ru.kotlix.frame.auth.server.token.dto.VerificationToken
import java.util.Base64

@Component
class VTokenDecoder(
    private val objectMapper: ObjectMapper,
    private val statefulByteCoder: StatefulByteCoder,
) : TokenDecoder<VerificationToken> {
    override fun getPayload(token: AccessToken): VerificationToken =
        try {
            readFromJson(statefulByteCoder.decode(base64UrlDecode(token)))
        } catch (ex: RuntimeException) {
            throw TokenDecoderException("Unable to decode token", ex)
        }

    override fun getSign(token: AccessToken): String {
        throw RuntimeException("This token does not require signing")
    }

    private fun readFromJson(json: ByteArray): VerificationToken = objectMapper.readValue(json, VerificationToken::class.java)

    private fun base64UrlDecode(data: String): ByteArray =
        data.replace(',', '=')
            .replace('_', '/')
            .replace('-', '+')
            .let { Base64.getDecoder().decode(it) }
}
