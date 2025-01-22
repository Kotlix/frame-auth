package ru.kotlix.frame.auth.server.token

import org.springframework.stereotype.Component
import ru.kotlix.frame.auth.server.token.dto.IdentifiedToken
import java.util.UUID

@Component
class TokenGeneratorImpl : TokenGenerator {
    override fun generateIdentifiedToken(
        id: Long,
        login: String,
    ): IdentifiedToken = IdentifiedToken(id, "$login:${UUID.randomUUID()}")

    override fun generateRandomToken(base: String): String = "${base}X${UUID.randomUUID()}"
}
