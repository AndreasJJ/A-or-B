package com.andreasjj.websocket

import com.andreasjj.websocket.annotation.OnAction
import com.andreasjj.websocket.types.*
import io.micronaut.scheduling.annotation.Scheduled
import io.micronaut.websocket.WebSocketBroadcaster
import io.micronaut.websocket.WebSocketSession
import io.micronaut.websocket.annotation.*
import org.reactivestreams.Publisher


@ServerWebSocket("/ws/game/{gameId}")
open class GameWebsocket(private val broadcaster: WebSocketBroadcaster) : WebSocket<GameClientMessage>(broadcaster, GameClientMessage::class) {
    @OnOpen
    fun onOpen(gameId: String, session: WebSocketSession?) {
        super.onOpen(session)
    }

    @OnMessage
    fun onMessage(gameId: String, message: String, session: WebSocketSession?) {
        super.onMessage(message, session)
    }

    @OnClose
    fun onClose(gameId: String, session: WebSocketSession?) {
        super.onClose(session)
    }

    @OnAction(action = ClientAction.STARTGAME)
    fun startGame(message: GameClientMessage, session: WebSocketSession?): Publisher<GameServerMessage>?  {
        val pingMessage = GameServerMessage(
            action = ServerAction.PING,
            text = "Ping"
        )
        return session?.send(pingMessage)
    }
}