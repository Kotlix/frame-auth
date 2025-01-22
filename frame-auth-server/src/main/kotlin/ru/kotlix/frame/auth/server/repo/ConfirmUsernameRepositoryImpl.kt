package ru.kotlix.frame.auth.server.repo

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.kotlix.frame.auth.server.repo.dto.ConfirmUsernameEntity
import ru.kotlix.frame.auth.server.repo.extension.queryForObjectOrNull
import java.sql.ResultSet
import java.time.OffsetDateTime

@Repository
class ConfirmUsernameRepositoryImpl(
    val jdbcTemplate: NamedParameterJdbcTemplate,
) : ConfirmUsernameRepository {
    companion object {
        val ROW_MAPPER =
            RowMapper { rs: ResultSet, _ ->
                ConfirmUsernameEntity(
                    id = rs.getLong("id"),
                    secret = rs.getString("secret"),
                    createdAt = rs.getObject("created_at", OffsetDateTime::class.java),
                    expiresAt = rs.getObject("expires_at", OffsetDateTime::class.java),
                    confirmed = rs.getBoolean("confirmed"),
                    userId = rs.getLong("user_id"),
                    newUsername = rs.getString("new_username"),
                )
            }
    }

    override fun findById(id: Long): ConfirmUsernameEntity? {
        return jdbcTemplate.queryForObjectOrNull(
            """
            select * from confirm_profile_username where id = :id;
            """.trimIndent(),
            mapOf(
                "id" to id,
            ),
            ROW_MAPPER,
        )
    }

    override fun findLast(authId: Long): ConfirmUsernameEntity? {
        return jdbcTemplate.queryForObjectOrNull(
            """
            select * from confirm_profile_username
                where user_id = :user_id
            order by created_at desc
                limit 1;
            """.trimIndent(),
            mapOf(
                "user_id" to authId,
            ),
            ROW_MAPPER,
        )
    }

    override fun setConfirmed(entity: ConfirmUsernameEntity): ConfirmUsernameEntity {
        return jdbcTemplate.queryForObject(
            """
            update confirm_profile_username
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

    override fun save(entity: ConfirmUsernameEntity): ConfirmUsernameEntity {
        return jdbcTemplate.queryForObject(
            """
            insert into confirm_profile_username (secret, expires_at, user_id, new_username)
            values (:secret,:expires_at,:user_id,:new_username)
            returning *;
            """.trimIndent(),
            mapOf(
                "secret" to entity.secret,
                "expires_at" to entity.expiresAt,
                "user_id" to entity.userId,
                "new_username" to entity.newUsername,
            ),
            ROW_MAPPER,
        )!!
    }
}
