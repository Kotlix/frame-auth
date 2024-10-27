package ru.enzhine.frame.auth.server.repo

import ru.enzhine.frame.auth.server.repo.dto.TokenEntity

interface TokenRepository {

    fun findTokenByAuthId(authId: Long): TokenEntity?

    fun upsertToken(token: TokenEntity): TokenEntity
}