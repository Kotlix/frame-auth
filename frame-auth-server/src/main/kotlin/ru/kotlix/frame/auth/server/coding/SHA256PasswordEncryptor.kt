package ru.kotlix.frame.auth.server.coding

import org.springframework.stereotype.Component
import java.security.MessageDigest

@Component
class SHA256PasswordEncryptor : PasswordEncryptor {
    override fun encrypt(password: String): String {
        val md =
            MessageDigest.getInstance("SHA-256").apply {
                update(password.toByteArray(Charsets.UTF_8))
            }
        return md.digest().joinToString("") { "%02x".format(it) }
    }
}
