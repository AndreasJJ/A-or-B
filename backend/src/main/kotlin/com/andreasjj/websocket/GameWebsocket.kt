package com.andreasjj.websocket

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.micronaut.scheduling.annotation.Scheduled
import io.micronaut.websocket.WebSocketBroadcaster
import io.micronaut.websocket.WebSocketSession
import io.micronaut.websocket.annotation.*
import org.reactivestreams.Publisher
import java.util.function.Predicate
import javax.validation.Valid

@ServerWebSocket("/ws/game/{gameId}/{username}")
open class GameWebsocket(private val broadcaster: WebSocketBroadcaster) {
    @OnOpen
    fun onOpen(gameId: String, username: String, session: WebSocketSession?): Publisher<GameServerMessage>? {
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
        session: WebSocketSession?
    ): Publisher<String>? {
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
        session: WebSocketSession?
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
        session: WebSocketSession?,
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
        session: WebSocketSession?,
        error: NullPointerException?
    ): Publisher<GameServerMessage>? {
        val newMessage = GameServerMessage(
            type = GameServerMessageType.ERROR,
            text = "Invalid Message"
        )
        return session?.send(newMessage)
    }

    private fun isValid(gameId: String): Predicate<WebSocketSession> {
        return Predicate { s: WebSocketSession ->
            gameId.equals(
                s.uriVariables.get(
                    "gameId",
                    String::class.java, null
                ), ignoreCase = true
            )
        }
    }
}