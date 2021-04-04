package com.andreasjj

import com.andreasjj.repository.GameRepository
import io.micronaut.http.HttpResponse

import javax.inject.Inject

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.annotation.Controller
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import java.time.Instant
import java.util.*

@Controller("/game")
@Secured(SecurityRule.IS_AUTHENTICATED)
class GameController {
    @Inject
    lateinit var gameRepository: GameRepository

    @Post("/new")
    fun add(): HttpResponse<Game> {
        var newGame = Game(
            owner = "",
            updateTimestamp = Instant.now(),
            createdTimestamp = Instant.now(),
            title = "",
            leftText = "",
            rightText = "",
            rounds = listOf<Round>(),
        )
        var createdGame = gameRepository.save(newGame)
        return HttpResponse.ok(createdGame)
    }

    @Get("/{id}")
    fun findById(@PathVariable id: String): HttpResponse<Game> {
        var game = gameRepository.find(UUID.fromString(id))
        return HttpResponse.ok(game)
    }

    @Get("/list")
    fun findAll(): HttpResponse<List<Game>> {
        var games = gameRepository.findAll()
        return HttpResponse.ok(games.toList())
    }
}