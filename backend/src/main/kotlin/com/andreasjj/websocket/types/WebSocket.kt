package com.andreasjj.websocket.types

import com.andreasjj.websocket.annotation.OnAction
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import com.google.gson.Gson
import io.micronaut.context.ApplicationContext
import io.micronaut.inject.qualifiers.Qualifiers
import io.micronaut.websocket.WebSocketBroadcaster
import io.micronaut.websocket.WebSocketSession
import io.micronaut.websocket.annotation.OnClose
import io.micronaut.websocket.annotation.OnMessage
import io.micronaut.websocket.annotation.OnOpen
import org.reactivestreams.Publisher
import java.util.function.Predicate
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.functions

open class WebSocket<out U: Message>(private val broadcaster: WebSocketBroadcaster, private val type: KClass<U>) {

    open fun onOpen(session: WebSocketSession?): Publisher<GameServerMessage>? {
        val newMessage = GameServerMessage(
            action = ServerAction.WELCOME,
            text = "Welcome!"
        )
        return session?.send(newMessage)
    }

    open fun onMessage(
        message: String,
        session: WebSocketSession?
    ): Publisher<Message>? {
        val result = validateMessage(message)
        val clientMessage: U = result.getOrElse {
            val newMessage = GameServerMessage(
                action = ServerAction.ERROR,
                text = "Invalid Message"
            )
            return session?.send(newMessage)
        }
        return route(clientMessage, session)
    }

    open fun onClose(
        session: WebSocketSession?
    ): Publisher<GameServerMessage>? {
        val newMessage = GameServerMessage(
            action = ServerAction.END,
            text = "Bye!"
        )
        return session?.send(newMessage)
    }

    private fun route(message: Message, session: WebSocketSession?): Publisher<Message>? {
        for (function in this::class.declaredMemberFunctions) {
            val isOnActionFunction = function.annotations.any { it -> it is OnAction }
            if (isOnActionFunction) {
                for (par in function.parameters) {
                    println(par)
                }
                function.call(this, message, session)
            }
        }

        val newMessage = GameServerMessage(
            action = ServerAction.ERROR,
            text = "Invalid Message"
        )
        return session?.send(newMessage)
    }

    private inline fun validateMessage(message: String): Result<U> {
        return try {
            var gson = Gson()
            val messageObject = gson.fromJson(message, type::class.java)
            messageObject.objectInstance.let {
                return Result.success(it)
            }
            Result.failure<T>(NullPointerException())
        } catch(e: KlaxonException) {
            Result.failure<T>(e)
        } catch(e: IllegalArgumentException) {
            Result.failure<T>(e)
        }
    }

    /*fun isValid(gameId: String): Predicate<WebSocketSession> {
        return Predicate { s: WebSocketSession ->
            gameId.equals(
                s.uriVariables.get(
                    "gameId",
                    String::class.java, null
                ), ignoreCase = true
            )
        }
    }*/
}