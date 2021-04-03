package com.andreasjj.manager

import com.andreasjj.Ticket
import com.andreasjj.repository.TicketRepository
import io.micronaut.context.event.ApplicationEventPublisher
import java.util.*
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
open class TicketManager(private val ticketRepository: TicketRepository) {
    @Transactional
    open fun validateTicket(id: UUID): Ticket? {
        val ticket = ticketRepository.findByIdForUpdate(id)
        ticket?.let {
            if (!it.used) {
                it.used = true
                ticketRepository.update(it)
                return it
            }
        }
        return null
    }

    open fun save(ticket: Ticket): Ticket {
        return ticketRepository.save(ticket);
    }
}