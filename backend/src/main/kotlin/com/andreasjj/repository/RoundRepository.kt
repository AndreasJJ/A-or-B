package com.andreasjj.repository

import com.andreasjj.Round

import io.micronaut.context.annotation.Executable
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
interface RoundRepository : CrudRepository<Round, Long> {
    @Executable
    fun find(roundId: Long): Round
}