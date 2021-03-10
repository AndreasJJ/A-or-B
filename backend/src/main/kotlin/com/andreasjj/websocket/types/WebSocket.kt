package com.andreasjj.websocket.types

import com.andreasjj.websocket.GameWebsocket
import com.andreasjj.websocket.annotation.OnAction
import com.google.gson.Gson
import io.micronaut.websocket.WebSocketBroadcaster
import io.micronaut.websocket.WebSocketSession
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.function.Predicate
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.*

open class WebSocket<out U: Message>(private val broadcaster: WebSocketBroadcaster, private val type: Class<U>) {
    val LOG: Logger = LoggerFactory.getLogger(GameWebsocket::class.java)

    open fun onOpen(session: WebSocketSession?): Publisher<GameServerMessage>? {
        val newMessage = GameServerMessage(
            action = ServerAction.WELCOME,
            text = "Welcome!"
        )
        return session?.send(newMessage)
    }

    open fun onMessage(
        message: String,
        session: WebSocketSession?,
        uriArgs: Map<String, String>
    ): Publisher<*>? {
        val result = validateMessage(message)
        val clientMessage: U = result.getOrElse {
            val newMessage = GameServerMessage(
                action = ServerAction.ERROR,
                text = "Invalid Message"
            )
            return session?.send(newMessage)
        }
        return route(uriArgs, clientMessage, session)
    }

    open fun onMessage(
        message: String,
        session: WebSocketSession?,
    ): Publisher<*>? {
        return onMessage(message, session, emptyMap<String, String>())
    }

    /*open fun onClose(
        session: WebSocketSession?
    ): Publisher<GameServerMessage>? {
        val newMessage = GameServerMessage(
            action = ServerAction.END,
            text = "Bye!"
        )
        return session?.send(newMessage)
    }*/

    private fun route(uriArgs: Map<String, String>, message: Message, session: WebSocketSession?): Publisher<*>? {
        // Route over all functions in the websocket controller
        // TODO: loop over all functions with the annotation, not just 'this'
        for (function in this::class.declaredMemberFunctions) {
            // Check that the functions has an annotation
            if (function.hasAnnotation<OnAction>()) {
                // Find the onAction annotation on the function
                val annotation = function.annotations.find { it is OnAction } as? OnAction
                // If the annotation action doesnt match the clientMessage then continue to the next function
                if (annotation?.action != message.action) {
                    continue
                }
                // Get the uri arguments from the annotation
                val annArgs = annotation.args
                // The uri arguments to be added to the call
                val chosenArgs = mutableListOf<String>()

                // If the parameter in the function is in the annotation arguments and the uri arguments then
                // add it to the arguments to be added to the call
                if (uriArgs.isNotEmpty()) {
                    for (par in function.parameters) {
                        val uriArg = uriArgs[par.name]
                        if (par.name in annArgs && !uriArg.isNullOrBlank()) {
                            par.name?.let { chosenArgs.add(uriArg) }
                        }
                    }
                }

                // Check that the function return type only have 1 argument as it should be Publisher<Message>
                if (function.returnType.arguments.size == 1) {
                    // We need to create the types to check that the type and its argument is correct
                    val returnTypeArgType = Message::class.createType()
                    val returnTypeType = Publisher::class.createType(listOf(KTypeProjection.STAR))
                    val isReturnTypeArgMessage = function.returnType.arguments[0].type?.isSubtypeOf(returnTypeArgType)
                    val isReturnTypeTypePublisher = function.returnType.classifier == returnTypeType.classifier
                    // TODO: This should probably be done better... but it works for now
                    if (isReturnTypeArgMessage == true && isReturnTypeTypePublisher) {
                        // If arguments are chosen, use them in the call. If not, don't
                        return if (chosenArgs.isNotEmpty()) {
                            val argsArray = chosenArgs.toTypedArray()
                            function.call(this, *argsArray, message, session) as Publisher<*>?
                        } else {
                            function.call(this, message, session) as Publisher<*>?
                        }
                    } else {
                        LOG.error("Return type of a function annotated with OnAction should be Publisher<*>?")
                    }
                }
            }
        }

        LOG.debug("Something went wrong in the routing or the client action was invalid")
        // If something went wrong, send a generic error message back
        val newMessage = GameServerMessage(
            action = ServerAction.ERROR,
            text = "Invalid Message"
        )
        return session?.send(newMessage)
    }

    private fun validateMessage(message: String): Result<U> {
        return try {
            val gson = Gson()
            val messageObject = gson.fromJson(message, type)
            return Result.success(messageObject)
        } catch(e: Exception) {
            println(e.message)
            Result.failure(e)
        }
    }

    fun isValid(id: String, arg: String): Predicate<WebSocketSession> {
        return Predicate { s: WebSocketSession ->
            id.equals(
                s.uriVariables.get(
                    arg,
                    String::class.java, null
                ), ignoreCase = true
            )
        }
    }
}