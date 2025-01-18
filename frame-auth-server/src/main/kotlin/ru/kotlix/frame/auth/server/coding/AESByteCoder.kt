package ru.kotlix.frame.auth.server.coding

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@Component
class AESByteCoder(
    @Value("\${token.secret-key}")
    secretKey: String,
) : StatefulByteCoder {
    companion object {
        private const val ALGO = "AES"
    }

    private val key = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), ALGO)

    override fun encode(byteArray: ByteArray): ByteArray = getCipher(true).doFinal(byteArray)

    override fun decode(byteArray: ByteArray): ByteArray = getCipher(false).doFinal(byteArray)

    private fun getCipher(enc: Boolean): Cipher {
        return Cipher.getInstance(ALGO).apply {
            init(if (enc) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE, key)
        }
    }
}
