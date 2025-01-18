package ru.kotlix.frame.auth.server.repo

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.kotlix.frame.auth.server.repo.dto.TokenEntity
import ru.kotlix.frame.auth.server.repo.extension.queryForObjectOrNull
import java.sql.ResultSet
import java.time.OffsetDateTime

@Repository
class TokenRepositoryImpl(
    val jdbcTemplate: NamedParameterJdbcTemplate,
) : TokenRepository {
    companion object {
        val ROW_MAPPER =
            RowMapper { rs: ResultSet, _ ->
                TokenEntity(
                    content = rs.getString("content"),
                    userAuthId = rs.getLong("user_auth_id"),
                    expiresAt = rs.getObject("expires_at", OffsetDateTime::class.java),
                )
            }
    }

    override fun findTokenByAuthId(authId: Long): TokenEntity? {
        return jdbcTemplate.queryForObjectOrNull(
            """
            select * from token where user_auth_id = :user_auth_id;
            """.trimIndent(),
            mapOf(
                "user_auth_id" to authId,
            ),
            ROW_MAPPER,
        )
    }

    override fun upsertToken(token: TokenEntity): TokenEntity {
        return jdbcTemplate.queryForObject(
            """
            insert into token (content, user_auth_id, expires_at)
                values (:content,:user_auth_id,:expires_at)
            on conflict (user_auth_id) do update set
                content = :content,
                user_auth_id = :user_auth_id,
                expires_at = :expires_at
            returning *;
            """.trimIndent(),
            mapOf(
                "content" to token.content,
                "user_auth_id" to token.userAuthId,
                "expires_at" to token.expiresAt,
            ),
            ROW_MAPPER,
        )!!
    }
}
