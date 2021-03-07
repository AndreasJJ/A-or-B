package com.andreasjj.websocket

import com.andreasjj.websocket.extension.ExtendedWebSocketBroadcaster
import com.andreasjj.websocket.extension.ExtendedWebSocketSession
import com.andreasjj.websocket.extension.annotation.*
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.micronaut.scheduling.annotation.Scheduled
import io.micronaut.validation.validator.Validator
import org.reactivestreams.Publisher
import java.util.function.Predicate
import javax.inject.Inject
import javax.validation.Valid

@ServerWebSocket("/ws/game/{gameId}/{username}")
open class GameWebsocket(private val broadcaster: ExtendedWebSocketBroadcaster) {
    @Inject
    lateinit var validator: Validator

    @OnOpen
    fun onOpen(gameId: String, username: String, session: ExtendedWebSocketSession?): Publisher<GameServerMessage>? {
        println(gameId)
        val newMessage = GameServerMessage(
            type = GameServerMessageType.WELCOME,
            text = "Welcome!"
        )
        return broadcaster.broadcast(newMessage, isValid(gameId))
    }

    @OnMessage
    open fun onMessage(
        gameId: String,
        username: String,
        @Valid message: GameClientMessage,
        session: ExtendedWebSocketSession?
    ): Publisher<String>? {
        println(message)
        val violations = validator.validate(message)
        println(violations.size)
        println(violations)
        val msg = "[$username] $message"
        return broadcaster.broadcast(msg, isValid(gameId))
    }

    @Scheduled(fixedDelay = "1s")
    fun ping(gameId: String, username: String): Publisher<GameServerMessage>? {
        println(gameId)
        val pingMessage = GameServerMessage(
            type = GameServerMessageType.PING,
            text = "Ping"
        )
        return broadcaster.broadcast(pingMessage)
    }

    @OnClose
    fun onClose(
        gameId: String,
        username: String,
        session: ExtendedWebSocketSession?
    ): Publisher<GameServerMessage>? {
        val newMessage = GameServerMessage(
            type = GameServerMessageType.END,
            text = "Bye!"
        )
        return broadcaster.broadcast(newMessage, isValid(gameId))
    }

    @OnError
    fun onError(
        gameId: String,
        username: String,
        session: ExtendedWebSocketSession?,
        error: MismatchedInputException?
    ): Publisher<GameServerMessage>? {
        val newMessage = GameServerMessage(
            type = GameServerMessageType.ERROR,
            text = "Invalid Message"
        )
        return session?.send(newMessage)
    }

    @OnError
    fun onError(
        gameId: String,
        username: String,
        session: ExtendedWebSocketSession?,
        error: NullPointerException?
    ): Publisher<GameServerMessage>? {
        val newMessage = GameServerMessage(
            type = GameServerMessageType.ERROR,
            text = "Invalid Message"
        )
        return session?.send(newMessage)
    }

    private fun isValid(gameId: String): Predicate<ExtendedWebSocketSession> {
        return Predicate { s: ExtendedWebSocketSession ->
            gameId.equals(
                s.uriVariables.get(
                    "gameId",
                    String::class.java, null
                ), ignoreCase = true
            )
        }
    }
}