package ru.enzhine.frame.auth.server.repo.extension

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

fun <T> NamedParameterJdbcTemplate.queryForObjectOrNull(
    sql: String,
    paramMap: Map<String,*>,
    rowMapper: RowMapper<T>) = try {
            this.queryForObject(sql, paramMap, rowMapper)
        } catch (ex: EmptyResultDataAccessException) {
            null
        }