package ru.enzhine.frame.auth.server.repo

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.enzhine.frame.auth.server.repo.dto.UserAuthEntity
import ru.enzhine.frame.auth.server.repo.extension.queryForObjectOrNull
import java.sql.ResultSet
import java.time.OffsetDateTime

@Repository
class UserAuthRepositoryImpl(
    val jdbcTemplate: NamedParameterJdbcTemplate
) : UserAuthRepository {

    companion object {
        val ROW_MAPPER = RowMapper { rs: ResultSet, _ ->
            UserAuthEntity(
                id = rs.getLong("id"),
                login = rs.getString("login"),
                password = rs.getString("password"),
                banned = rs.getBoolean("banned"),
                verified = rs.getBoolean("verified"),
                createdAt = rs.getObject("created_at", OffsetDateTime::class.java),
                updatedAt = rs.getObject("updated_at", OffsetDateTime::class.java)
            )
        }
    }

    override fun save(userAuth: UserAuthEntity): UserAuthEntity {
        return jdbcTemplate.queryForObject("""
            insert into user_auth (login, password, verified)
            values (:login,:password,:verified)
            returning *;
        """.trimIndent(), mapOf(
            "login" to userAuth.login,
            "password" to userAuth.password,
            "verified" to userAuth.verified
        ), ROW_MAPPER)!!
    }

    override fun remove(userAuth: UserAuthEntity) {
        jdbcTemplate.update("""
            delete from user_auth
            where id = :id;
        """.trimIndent(), mapOf(
            "id" to userAuth.id!!
        ))
    }

    override fun findByLogin(login: String): UserAuthEntity? {
        return jdbcTemplate.queryForObjectOrNull("""
            select * from user_auth where login = :login;
        """.trimIndent(), mapOf(
            "login" to login
        ), ROW_MAPPER)
    }

    override fun findById(id: Long): UserAuthEntity? {
        return jdbcTemplate.queryForObjectOrNull("""
            select * from user_auth where id = :id;
        """.trimIndent(), mapOf(
            "id" to id
        ), ROW_MAPPER)
    }

    override fun updatePassword(userAuthId: Long, password: String): UserAuthEntity {
        return jdbcTemplate.queryForObject("""
            update user_auth
            set password = :password,
                updated_at = now()
            where id = :id
            returning *;
        """.trimIndent(), mapOf(
            "password" to password,
            "id" to userAuthId
        ), ROW_MAPPER)!!
    }

    override fun setVerified(userAuthEntity: UserAuthEntity): UserAuthEntity {
        return jdbcTemplate.queryForObject("""
            update user_auth
            set verified = true,
                updated_at = now()
            where id = :id
            returning *;
        """.trimIndent(), mapOf(
            "id" to userAuthEntity.id!!
        ), ROW_MAPPER)!!
    }
}