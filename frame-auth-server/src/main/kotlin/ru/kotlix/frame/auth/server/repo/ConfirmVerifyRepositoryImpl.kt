package ru.kotlix.frame.auth.server.repo

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.kotlix.frame.auth.server.repo.dto.ConfirmVerifyEntity
import ru.kotlix.frame.auth.server.repo.extension.queryForObjectOrNull
import java.sql.ResultSet
import java.time.OffsetDateTime

@Repository
class ConfirmVerifyRepositoryImpl(
    val jdbcTemplate: NamedParameterJdbcTemplate,
) : ConfirmVerifyRepository {
    companion object {
        val ROW_MAPPER =
            RowMapper { rs: ResultSet, _ ->
                ConfirmVerifyEntity(
                    id = rs.getLong("id"),
                    secret = rs.getString("secret"),
                    createdAt = rs.getObject("created_at", OffsetDateTime::class.java),
                    expiresAt = rs.getObject("expires_at", OffsetDateTime::class.java),
                    confirmed = rs.getBoolean("confirmed"),
                    userId = rs.getLong("user_id"),
                )
            }
    }

    override fun findById(id: Long): ConfirmVerifyEntity? {
        return jdbcTemplate.queryForObjectOrNull(
            """
            select * from confirm_user_verify where id = :id;
            """.trimIndent(),
            mapOf(
                "id" to id,
            ),
            ROW_MAPPER,
        )
    }

    override fun findByAuthId(authId: Long): ConfirmVerifyEntity? {
        return jdbcTemplate.queryForObjectOrNull(
            """
            select * from confirm_user_verify where user_id = :user_id;
            """.trimIndent(),
            mapOf(
                "user_id" to authId,
            ),
            ROW_MAPPER,
        )
    }

    override fun setConfirmed(entity: ConfirmVerifyEntity): ConfirmVerifyEntity {
        return jdbcTemplate.queryForObject(
            """
            update confirm_user_verify
            set confirmed = true
            where id = :id
            returning *;
            """.trimIndent(),
            mapOf(
                "id" to entity.id,
            ),
            ROW_MAPPER,
        )!!
    }

    override fun save(entity: ConfirmVerifyEntity): ConfirmVerifyEntity {
        return jdbcTemplate.queryForObject(
            """
            insert into confirm_user_verify (secret, expires_at, user_id)
            values (:secret,:expires_at,:user_id)
            returning *;
            """.trimIndent(),
            mapOf(
                "secret" to entity.secret,
                "expires_at" to entity.expiresAt,
                "user_id" to entity.userId,
            ),
            ROW_MAPPER,
        )!!
    }

    override fun remove(entity: ConfirmVerifyEntity) {
        jdbcTemplate.update(
            """
            delete from confirm_user_verify
            where id = :id;
            """.trimIndent(),
            mapOf(
                "id" to entity.id!!,
            ),
        )
    }
}
