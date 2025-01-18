package ru.kotlix.frame.auth.server.repo

import ru.kotlix.frame.auth.server.repo.dto.ConfirmUsernameEntity

interface ConfirmUsernameRepository {
    fun findById(id: Long): ConfirmUsernameEntity?

    fun findLast(authId: Long): ConfirmUsernameEntity?

    fun setConfirmed(entity: ConfirmUsernameEntity): ConfirmUsernameEntity

    fun save(entity: ConfirmUsernameEntity): ConfirmUsernameEntity
}
