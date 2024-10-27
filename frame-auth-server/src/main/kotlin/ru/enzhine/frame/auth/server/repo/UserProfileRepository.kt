package ru.enzhine.frame.auth.server.repo

import ru.enzhine.frame.auth.server.repo.dto.UserProfileEntity

interface UserProfileRepository {

    fun findByAuthId(authId: Long): UserProfileEntity?

    fun findByEmail(email: String): UserProfileEntity?

    fun updateUsername(userAuthId: Long, username: String): UserProfileEntity

    fun updateEmail(userAuthId: Long, email: String): UserProfileEntity

    fun save(userProfile: UserProfileEntity): UserProfileEntity

    fun remove(userProfile: UserProfileEntity)
}