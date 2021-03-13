package com.andreasjj.repository

import com.andreasjj.Ticket

import io.micronaut.context.annotation.Executable
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.util.*

import javax.transaction.Transactional

@JdbcRepository(dialect = Dialect.POSTGRES)
interface TicketRepository : CrudRepository<Ticket, UUID> {
    @Executable
    fun find(id: UUID): Ticket

    @Executable
    fun findByIdForUpdate(id: UUID): Ticket?

    @Transactional
    fun validateTicket(id: UUID): Ticket? {
        val ticket = findByIdForUpdate(id)
        ticket?.let {
            if (!it.used) {
                it.used = true
                update(it)
                return it
            }
        }
        return null
    }
}