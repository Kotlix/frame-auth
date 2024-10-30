package ru.enzhine.frame.auth.server.repo

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.enzhine.frame.auth.server.repo.dto.ConfirmPasswordEntity
import ru.enzhine.frame.auth.server.repo.extension.queryForObjectOrNull
import java.sql.ResultSet
import java.time.OffsetDateTime

@Repository
class ConfirmPasswordRepositoryImpl(
    val jdbcTemplate: NamedParameterJdbcTemplate
) : ConfirmPasswordRepository {

    companion object {
        val ROW_MAPPER = RowMapper { rs: ResultSet, _ ->
            ConfirmPasswordEntity(
                id = rs.getLong("id"),
                secret = rs.getString("secret"),
                createdAt = rs.getObject("created_at", OffsetDateTime::class.java),
                expiresAt = rs.getObject("expires_at", OffsetDateTime::class.java),
                confirmed = rs.getBoolean("confirmed"),
                userId = rs.getLong("user_id"),
                newPassword = rs.getString("new_password")
            )
        }
    }

    override fun findById(id: Long): ConfirmPasswordEntity? {
        return jdbcTemplate.queryForObjectOrNull("""
            select * from confirm_user_password where id = :id;
        """.trimIndent(), mapOf(
            "id" to id
        ), ROW_MAPPER)
    }

    override fun findLast(authId: Long): ConfirmPasswordEntity? {
        return jdbcTemplate.queryForObjectOrNull("""
            select * from confirm_user_password
                where user_id = :user_id
            order by created_at desc
                limit 1;
        """.trimIndent(), mapOf(
            "user_id" to authId
        ), ROW_MAPPER)
    }

    override fun setConfirmed(entity: ConfirmPasswordEntity): ConfirmPasswordEntity {
        return jdbcTemplate.queryForObject("""
            update confirm_user_password
            set confirmed = true
            where id = :id
            returning *;
        """.trimIndent(), mapOf(
            "id" to entity.id
        ), ROW_MAPPER)!!
    }

    override fun save(entity: ConfirmPasswordEntity): ConfirmPasswordEntity {
        return jdbcTemplate.queryForObject("""
            insert into confirm_user_password (secret, expires_at, user_id, new_password)
            values (:secret,:expires_at,:user_id,:new_password)
            returning *;
        """.trimIndent(), mapOf(
            "secret" to entity.secret,
            "expires_at" to entity.expiresAt,
            "user_id" to entity.userId,
            "new_password" to entity.newPassword
        ), ROW_MAPPER)!!
    }
}