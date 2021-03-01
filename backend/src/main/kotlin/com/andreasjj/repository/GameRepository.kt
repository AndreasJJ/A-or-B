package com.andreasjj.repository

import com.andreasjj.Game

import io.micronaut.context.annotation.Executable
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.util.*

@JdbcRepository(dialect = Dialect.POSTGRES)
interface GameRepository : CrudRepository<Game, UUID> {
    @Executable
    fun find(id: UUID): Game
}