package ru.enzhine.frame.auth.server.repo

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.enzhine.frame.auth.server.repo.dto.UserProfileEntity
import ru.enzhine.frame.auth.server.repo.extension.queryForObjectOrNull
import java.sql.ResultSet
import java.time.OffsetDateTime

@Repository
class UserProfileRepositoryImpl(
    val jdbcTemplate: NamedParameterJdbcTemplate
) : UserProfileRepository {

    companion object {
        val ROW_MAPPER = RowMapper { rs: ResultSet, _ ->
            UserProfileEntity(
                username = rs.getString("username"),
                email = rs.getString("email"),
                userAuthId = rs.getLong("user_auth_id"),
                updatedAt = rs.getObject("updated_at", OffsetDateTime::class.java)
            )
        }
    }

    override fun findByAuthId(authId: Long): UserProfileEntity? {
        return jdbcTemplate.queryForObjectOrNull("""
            select * from user_profile where user_auth_id = :user_auth_id;
        """.trimIndent(), mapOf(
            "user_auth_id" to authId
        ), ROW_MAPPER)
    }

    override fun findByEmail(email: String): UserProfileEntity? {
        return jdbcTemplate.queryForObjectOrNull("""
            select * from user_profile where email = :email;
        """.trimIndent(), mapOf(
            "email" to email
        ), ROW_MAPPER)
    }

    override fun updateUsername(userAuthId: Long, username: String): UserProfileEntity {
        return jdbcTemplate.queryForObject("""
            update user_profile
            set username = :username,
                updated_at = now()
            where user_auth_id = :user_auth_id
            returning *;
        """.trimIndent(), mapOf(
            "user_auth_id" to userAuthId,
            "username" to username
        ), ROW_MAPPER)!!
    }

    override fun updateEmail(userAuthId: Long, email: String): UserProfileEntity {
        return jdbcTemplate.queryForObject("""
            update user_profile
            set email = :email,
                updated_at = now()
            where user_auth_id = :user_auth_id
            returning *;
        """.trimIndent(), mapOf(
            "user_auth_id" to userAuthId,
            "email" to email
        ), ROW_MAPPER)!!
    }

    override fun save(userProfile: UserProfileEntity): UserProfileEntity {
        return jdbcTemplate.queryForObject("""
            insert into user_profile (username, email, user_auth_id)
            values (:username,:email,:user_auth_id)
            returning *;
        """.trimIndent(), mapOf(
            "username" to userProfile.username,
            "email" to userProfile.email,
            "user_auth_id" to userProfile.userAuthId
        ), ROW_MAPPER)!!
    }

    override fun remove(userProfile: UserProfileEntity) {
        jdbcTemplate.update("""
            delete from user_profile
            where user_auth_id = :user_auth_id;
        """.trimIndent(), mapOf(
            "user_auth_id" to userProfile.userAuthId
        ))
    }
}