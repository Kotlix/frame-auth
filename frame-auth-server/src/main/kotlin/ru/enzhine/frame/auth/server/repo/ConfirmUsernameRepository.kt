package ru.enzhine.frame.auth.server.repo

import ru.enzhine.frame.auth.server.repo.dto.ConfirmUsernameEntity

interface ConfirmUsernameRepository {

    fun findById(id: Long): ConfirmUsernameEntity?

    fun setConfirmed(entity: ConfirmUsernameEntity): ConfirmUsernameEntity

    fun save(entity: ConfirmUsernameEntity): ConfirmUsernameEntity
}