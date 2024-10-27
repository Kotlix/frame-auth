package ru.enzhine.frame.auth.server.repo

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.enzhine.frame.auth.server.repo.dto.ConfirmEmailEntity
import ru.enzhine.frame.auth.server.repo.extension.queryForObjectOrNull
import java.sql.ResultSet
import java.time.OffsetDateTime

@Repository
class ConfirmEmailRepositoryImpl(
    val jdbcTemplate: NamedParameterJdbcTemplate
) : ConfirmEmailRepository {

    companion object {
        val ROW_MAPPER = RowMapper { rs: ResultSet, _ ->
            ConfirmEmailEntity(
                id = rs.getLong("id"),
                secret = rs.getString("secret"),
                createdAt = rs.getObject("created_at", OffsetDateTime::class.java),
                expiresAt = rs.getObject("expires_at", OffsetDateTime::class.java),
                confirmed = rs.getBoolean("confirmed"),
                userId = rs.getLong("user_id"),
                newEmail = rs.getString("new_email")
            )
        }
    }

    override fun findById(id: Long): ConfirmEmailEntity? {
        return jdbcTemplate.queryForObjectOrNull("""
            select * from confirm_profile_email where id = :id;
        """.trimIndent(), mapOf(
            "id" to id
        ), ROW_MAPPER)
    }

    override fun setConfirmed(entity: ConfirmEmailEntity): ConfirmEmailEntity {
        return jdbcTemplate.queryForObject("""
            update confirm_profile_email
            set confirmed = true
            where id = :id
            returning *;
        """.trimIndent(), mapOf(
            "id" to entity.id
        ), ROW_MAPPER)!!
    }

    override fun save(entity: ConfirmEmailEntity): ConfirmEmailEntity {
        return jdbcTemplate.queryForObject("""
            insert into confirm_profile_email (secret, expires_at, user_id, new_email)
            values (:secret,:expires_at,:user_id,:new_email)
            returning *;
        """.trimIndent(), mapOf(
            "secret" to entity.secret,
            "expires_at" to entity.expiresAt,
            "user_id" to entity.userId,
            "new_email" to entity.newEmail
        ), ROW_MAPPER)!!
    }
}