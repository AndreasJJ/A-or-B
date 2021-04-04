package com.andreasjj.websocket

import com.andreasjj.websocket.annotation.OnAction
import com.andreasjj.websocket.types.*
import io.micronaut.scheduling.annotation.Scheduled
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.websocket.WebSocketBroadcaster
import io.micronaut.websocket.WebSocketSession
import io.micronaut.websocket.annotation.*
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import io.micronaut.security.utils.SecurityService

import javax.inject.Inject




@ServerWebSocket("/ws/game/{gameId}")
@Secured(SecurityRule.IS_ANONYMOUS)
open class GameWebsocket(private val broadcaster: WebSocketBroadcaster) : WebSocket<GameClientMessage>(broadcaster, GameClientMessage::class.java) {
    @OnOpen
    fun onOpen(gameId: String, session: WebSocketSession?): Publisher<*>? {
        return super.onOpen(session)
    }

    @OnMessage
    fun onMessage(gameId: String, message: String, session: WebSocketSession?): Publisher<*>? {
        return super.onMessage(message, session, mapOf("gameId" to gameId))
    }

    /*@OnClose
    fun onClose(gameId: String, session: WebSocketSession?): Publisher<*>? {
        println(session)
        println(session?.isOpen)
        return super.onClose(session)
    }*/

    @OnAction(action = ClientAction.STARTGAME, auth = SecurityRule.IS_AUTHENTICATED)
    fun startGame(message: GameClientMessage, session: WebSocketSession?): Publisher<GameServerMessage>?  {
        val pingMessage = GameServerMessage(
            action = ServerAction.PING,
            text = "Start"
        )
        return session?.send(pingMessage)
    }

    @OnAction(action = ClientAction.JOINGAME)
    fun joinGame(message: GameClientMessage, session: WebSocketSession?): Publisher<GameServerMessage>?  {
        val pingMessage = GameServerMessage(
            action = ServerAction.PING,
            text = "Start"
        )
        return session?.send(pingMessage)
    }

    @OnAction(action = ClientAction.ANSWER)
    fun answerRound(message: GameClientMessage, session: WebSocketSession?): Publisher<GameServerMessage>?  {
        val pingMessage = GameServerMessage(
            action = ServerAction.PING,
            text = "Start"
        )
        return session?.send(pingMessage)
    }

    @OnAction(action = ClientAction.NEXTROUND, auth = SecurityRule.IS_AUTHENTICATED)
    fun nextRound(message: GameClientMessage, session: WebSocketSession?): Publisher<GameServerMessage>?  {
        val pingMessage = GameServerMessage(
            action = ServerAction.PING,
            text = "Start"
        )
        return session?.send(pingMessage)
    }

    @OnAction(action = ClientAction.SKIPROUND, auth = SecurityRule.IS_AUTHENTICATED)
    fun skipRound(message: GameClientMessage, session: WebSocketSession?): Publisher<GameServerMessage>?  {
        val pingMessage = GameServerMessage(
            action = ServerAction.PING,
            text = "Start"
        )
        return session?.send(pingMessage)
    }

    @OnAction(action = ClientAction.ENDGAME, ["gameId"], auth = SecurityRule.IS_AUTHENTICATED)
    fun endGame(gameId: String, message: GameClientMessage, session: WebSocketSession?): Publisher<GameServerMessage>?  {
        val pingMessage = GameServerMessage(
            action = ServerAction.PING,
            text = "End"
        )
        return broadcaster.broadcast(pingMessage, isValid(gameId, "gameId"))
    }
}