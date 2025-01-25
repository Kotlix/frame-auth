package ru.kotlix.frame.auth.server.token

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import ru.kotlix.frame.auth.api.AccessToken
import ru.kotlix.frame.auth.api.token.TokenEncoder
import ru.kotlix.frame.auth.server.coding.StatefulByteCoder
import ru.kotlix.frame.auth.server.token.dto.VerificationToken
import java.util.Base64

@Component
class VTokenEncoder(
    private val objectMapper: ObjectMapper,
    private val statefulByteCoder: StatefulByteCoder,
) : TokenEncoder<VerificationToken> {
    override fun encodeAndSign(userInfo: VerificationToken): AccessToken = base64UrlEncode(statefulByteCoder.encode(writeAsJson(userInfo)))

    private fun writeAsJson(obj: VerificationToken): ByteArray = objectMapper.writeValueAsBytes(obj)

    private fun base64UrlEncode(data: ByteArray): String =
        Base64.getEncoder().encodeToString(data)
            .replace('+', '-')
            .replace('/', '_')
            .replace('=', ',')
}
