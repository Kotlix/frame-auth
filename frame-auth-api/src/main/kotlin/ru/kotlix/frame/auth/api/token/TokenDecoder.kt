package ru.kotlix.frame.auth.api.token

import ru.kotlix.frame.auth.api.AccessToken
import ru.kotlix.frame.auth.api.token.exception.TokenDecoderException
import kotlin.jvm.Throws

interface TokenDecoder<T> {
    @Throws(TokenDecoderException::class)
    fun getPayload(token: AccessToken): T

    @Throws(TokenDecoderException::class)
    fun getSign(token: AccessToken): String
}
