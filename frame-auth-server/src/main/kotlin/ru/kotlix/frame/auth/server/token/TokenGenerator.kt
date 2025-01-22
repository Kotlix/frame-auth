package ru.kotlix.frame.auth.server.token

import ru.kotlix.frame.auth.server.token.dto.IdentifiedToken

interface TokenGenerator {
    fun generateIdentifiedToken(
        id: Long,
        login: String,
    ): IdentifiedToken

    fun generateRandomToken(base: String): String
}
