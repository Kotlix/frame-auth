package ru.kotlix.frame.auth.server.token

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.kotlix.frame.auth.api.token.TokenSigner
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Component
class HmacSHA256TokenSigner(
    @Value("\${token.secret-key}")
    secretKey: String,
) : TokenSigner {
    companion object {
        const val ALGO = "HmacSHA256"
    }

    private val key = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), ALGO)

    override fun sign(data: String): String {
        val hmac = Mac.getInstance(ALGO)
        hmac.init(key)
        return hmac.doFinal(data.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}
