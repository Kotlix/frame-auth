package ru.kotlix.frame.auth.server.repo

import ru.kotlix.frame.auth.server.repo.dto.UserAuthEntity

interface UserAuthRepository {
    fun findByLogin(login: String): UserAuthEntity?

    fun findById(id: Long): UserAuthEntity?

    fun updatePassword(
        userAuthId: Long,
        password: String,
    ): UserAuthEntity

    fun setVerified(userAuthEntity: UserAuthEntity): UserAuthEntity

    fun save(userAuth: UserAuthEntity): UserAuthEntity

    fun remove(userAuth: UserAuthEntity)
}
