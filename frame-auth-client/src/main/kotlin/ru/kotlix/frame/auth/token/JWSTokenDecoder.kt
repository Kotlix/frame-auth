package ru.kotlix.frame.auth.token

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import ru.kotlix.frame.auth.api.AccessToken
import ru.kotlix.frame.auth.api.token.TokenDecoder
import ru.kotlix.frame.auth.api.token.dto.UserInfo
import ru.kotlix.frame.auth.api.token.exception.TokenDecoderException
import java.util.Base64

@Component
class JWSTokenDecoder(
    private val objectMapper: ObjectMapper,
) : TokenDecoder<UserInfo> {
    override fun getPayload(token: AccessToken): UserInfo =
        try {
            val payload = token.split('.')[0]
            readFromJson(base64UrlDecode(payload))
        } catch (ex: RuntimeException) {
            throw TokenDecoderException("Unable to decode token", ex)
        }

    override fun getSign(token: AccessToken): String =
        try {
            token.split('.')[1]
        } catch (ex: RuntimeException) {
            throw TokenDecoderException("Unable to decode token", ex)
        }

    private fun readFromJson(json: ByteArray): UserInfo = objectMapper.readValue(json, UserInfo::class.java)

    private fun base64UrlDecode(data: String): ByteArray =
        data.replace(',', '=')
            .replace('_', '/')
            .replace('-', '+')
            .let { Base64.getDecoder().decode(it) }
}
