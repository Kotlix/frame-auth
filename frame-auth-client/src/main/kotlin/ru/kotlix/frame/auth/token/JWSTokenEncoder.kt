package ru.kotlix.frame.auth.token

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import ru.kotlix.frame.auth.api.AccessToken
import ru.kotlix.frame.auth.api.token.TokenEncoder
import ru.kotlix.frame.auth.api.token.TokenSigner
import ru.kotlix.frame.auth.api.token.dto.UserInfo
import java.util.Base64

@Component
class JWSTokenEncoder(
    private val objectMapper: ObjectMapper,
    private val tokenSigner: TokenSigner,
) : TokenEncoder<UserInfo> {
    override fun encodeAndSign(userInfo: UserInfo): AccessToken {
        val payload = base64UrlEncode(writeAsJson(userInfo))

        return payload + "." + tokenSigner.sign(payload)
    }

    private fun writeAsJson(obj: UserInfo): ByteArray = objectMapper.writeValueAsBytes(obj)

    private fun base64UrlEncode(data: ByteArray): String =
        Base64.getEncoder().encodeToString(data)
            .replace('+', '-')
            .replace('/', '_')
            .replace('=', ',')
}
