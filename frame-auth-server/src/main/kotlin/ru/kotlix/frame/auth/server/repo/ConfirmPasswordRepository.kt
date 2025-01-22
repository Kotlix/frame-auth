package ru.kotlix.frame.auth.server.repo

import ru.kotlix.frame.auth.server.repo.dto.ConfirmPasswordEntity

interface ConfirmPasswordRepository {
    fun findById(id: Long): ConfirmPasswordEntity?

    fun findLast(authId: Long): ConfirmPasswordEntity?

    fun setConfirmed(entity: ConfirmPasswordEntity): ConfirmPasswordEntity

    fun save(entity: ConfirmPasswordEntity): ConfirmPasswordEntity
}
