package ru.kotlix.frame.auth.server.token

import org.springframework.stereotype.Component
import ru.kotlix.frame.auth.server.coding.StatefulByteCoder
import ru.kotlix.frame.auth.server.token.dto.IdentifiedToken
import java.nio.ByteBuffer
import java.util.Base64

@Component
class TokenCoderImpl(
    val statefulByteCoder: StatefulByteCoder,
) : ru.kotlix.frame.auth.server.token.TokenCoder {
    override fun stringifyIdentifiedToken(identifiedToken: IdentifiedToken): String {
        val bb = ByteBuffer.wrap(ByteArray(8)).putLong(identifiedToken.id)

        return "${codeArray(bb.array())}:${codeString(identifiedToken.content)}"
    }

    private fun codeArray(array: ByteArray): String = b64urlEncoded(statefulByteCoder.encode(array))

    private fun codeString(string: String): String = b64urlEncoded(statefulByteCoder.encode(string.toByteArray(Charsets.UTF_8)))

    private fun decodeString(string: String): ByteArray = statefulByteCoder.decode(b64urlDecoded(string))

    override fun stringifyContent(content: String): String = codeString(content)

    override fun parseIdentifiedToken(str: String): IdentifiedToken {
        val arr = str.split(':')
        if (arr.size >= 2) {
            return IdentifiedToken(
                ByteBuffer.wrap(decodeString(arr[0])).getLong(),
                decodeString(arr[1]).toString(Charsets.UTF_8),
            )
        }
        throw IllegalArgumentException("Provided argument is not a token!")
    }

    fun b64urlEncoded(inp: ByteArray): String =
        Base64.getEncoder().encodeToString(inp)
            .replace('+', '-')
            .replace('/', '_')
            .replace('=', '.')

    fun b64urlDecoded(inp: String): ByteArray =
        inp
            .replace('.', '=')
            .replace('_', '/')
            .replace('-', '+')
            .let { Base64.getDecoder().decode(it) }
}
