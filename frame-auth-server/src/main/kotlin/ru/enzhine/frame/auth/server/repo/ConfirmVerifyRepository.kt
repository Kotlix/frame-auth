package ru.enzhine.frame.auth.server.repo

import ru.enzhine.frame.auth.server.repo.dto.ConfirmVerifyEntity

interface ConfirmVerifyRepository {

    fun findById(id: Long): ConfirmVerifyEntity?

    fun findByAuthId(authId: Long): ConfirmVerifyEntity?

    fun setConfirmed(entity: ConfirmVerifyEntity): ConfirmVerifyEntity

    fun save(entity: ConfirmVerifyEntity): ConfirmVerifyEntity

    fun remove(entity: ConfirmVerifyEntity)
}