package ru.enzhine.frame.auth.server.token

import ru.enzhine.frame.auth.server.token.dto.IdentifiedToken

interface TokenCoder {

    fun stringifyIdentifiedToken(identifiedToken: IdentifiedToken): String

    fun stringifyContent(content: String): String

    fun parseIdentifiedToken(str: String): IdentifiedToken
}