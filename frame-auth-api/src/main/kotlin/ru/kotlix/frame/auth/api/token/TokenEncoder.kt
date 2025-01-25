package ru.kotlix.frame.auth.api.token

import ru.kotlix.frame.auth.api.AccessToken

interface TokenEncoder<T> {
    fun encodeAndSign(userInfo: T): AccessToken
}
