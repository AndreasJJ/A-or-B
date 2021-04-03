package com.andreasjj

import com.andreasjj.manager.TicketManager
import com.andreasjj.repository.TicketRepository

import javax.inject.Inject

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.server.util.HttpClientAddressResolver
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.micronaut.security.utils.SecurityService

import java.time.Instant

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/ticket")
class TicketController {
    @Inject
    lateinit var ticketManager: TicketManager

    @Get("/")
    fun index(request: HttpRequest<*>, authentication: Authentication): HttpResponse<Ticket> {
        println(authentication.attributes)
        val sub = authentication.attributes["sub"] as? String
        println(sub)
        sub?.let {
            val newTicket = Ticket(sub = sub, updateTimestamp = Instant.now(), createdTimestamp = Instant.now())
            val createdTicket = ticketManager.save(newTicket);
            return HttpResponse.ok(createdTicket)
        }
        return HttpResponse.badRequest()
    }
}