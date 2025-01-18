package ru.kotlix.frame.auth.server.repo

import ru.kotlix.frame.auth.server.repo.dto.ConfirmEmailEntity

interface ConfirmEmailRepository {
    fun findById(id: Long): ConfirmEmailEntity?

    fun findLast(authId: Long): ConfirmEmailEntity?

    fun setConfirmed(entity: ConfirmEmailEntity): ConfirmEmailEntity

    fun save(entity: ConfirmEmailEntity): ConfirmEmailEntity
}
