package ru.kotlix.frame.auth.api.token

interface TokenSigner {
    fun sign(data: String): String
}
