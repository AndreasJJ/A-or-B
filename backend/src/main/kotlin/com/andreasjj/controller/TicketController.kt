package com.andreasjj

import com.andreasjj.repository.TicketRepository

import javax.inject.Inject

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.server.util.HttpClientAddressResolver

import java.time.Instant


@Controller("/ticket")
class TicketController {
    @Inject
    lateinit var ticketRepository: TicketRepository

    @Inject
    lateinit var httpClientAddressResolver: HttpClientAddressResolver

    @Get("/")
    fun index(request: HttpRequest<*>): HttpResponse<Ticket> {
        var ip = httpClientAddressResolver.resolve(request) ?: return HttpResponse.badRequest()
        var newTicket = Ticket(token = "", ip = ip, updateTimestamp = Instant.now(), createdTimestamp = Instant.now())
        var createdTicket = ticketRepository.save(newTicket);
        return HttpResponse.ok(createdTicket)
    }
}