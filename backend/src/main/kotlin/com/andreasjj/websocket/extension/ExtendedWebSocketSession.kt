package com.andreasjj.websocket.extension

import io.micronaut.context.annotation.Replaces
import io.micronaut.websocket.CloseReason
import io.micronaut.websocket.WebSocketSession
import io.micronaut.core.convert.value.ConvertibleValues
import io.micronaut.core.convert.value.ConvertibleMultiValues
import java.util.concurrent.CompletableFuture
import org.reactivestreams.Publisher
import io.micronaut.websocket.exceptions.WebSocketSessionException
import java.util.concurrent.ExecutionException
import io.micronaut.core.convert.value.MutableConvertibleValues
import io.micronaut.http.MediaType
import java.net.URI
import java.security.Principal
import java.util.*

@Replaces(WebSocketSession::class)
interface ExtendedWebSocketSession : MutableConvertibleValues<Any?>, AutoCloseable {
    /**
     * The ID of the session.
     *
     * @return The ID of the session
     */
    val id: String?

    /**
     * @return Only the attributes of the session
     */
    val attributes: MutableConvertibleValues<Any?>?

    /**
     * Whether the session is open.
     * @return True if it is
     */
    val isOpen: Boolean

    /**
     * Whether the session is writable. It may not be writable, if the buffer is currently full
     * @return True if it is
     */
    val isWritable: Boolean

    /**
     * Whether the connection is secure.
     *
     * @return True if it is secure
     */
    val isSecure: Boolean

    /**
     * The current open sessions.
     *
     * @return The open sessions
     */
    val openSessions: Set<WebSocketSession?>?

    /**
     * The request URI this session was opened under.
     *
     * @return The request URI
     */
    val requestURI: URI?

    /**
     * The protocol version of the WebSocket protocol currently being used.
     *
     * @return The protocol version
     */
    val protocolVersion: String?

    /**
     * Send the given message to the remote peer.
     * The resulting [Publisher] does not start sending until subscribed to.
     * If you return it from Micronaut annotated methods such as [io.micronaut.websocket.annotation.OnOpen] and [io.micronaut.websocket.annotation.OnMessage],
     * Micronaut will subscribe to it and send the message without blocking.
     *
     * @param message The message
     * @param mediaType The media type of the message. Used to lookup an appropriate codec via the [io.micronaut.http.codec.MediaTypeCodecRegistry].
     * @param <T> The message type
     * @return A [Publisher] that either emits an error or emits the message once it has been published successfully.
    </T> */
    fun <T> send(message: T, mediaType: MediaType?): Publisher<T>?

    /**
     * Send the given message to the remote peer asynchronously.
     *
     * @param message The message
     * @param mediaType The media type of the message. Used to lookup an appropriate codec via the [io.micronaut.http.codec.MediaTypeCodecRegistry].
     * @param <T> The message type
     * @return A [CompletableFuture] that tracks the execution. [CompletableFuture.get] and related methods will return the message on success, on error throw the underlying Exception.
    </T> */
    fun <T> sendAsync(message: T, mediaType: MediaType?): CompletableFuture<T>
    /**
     * Send the given message to the remote peer synchronously.
     *
     * @param message The message
     * @param mediaType The media type of the message. Used to lookup an appropriate codec via the [io.micronaut.http.codec.MediaTypeCodecRegistry].
     */
    /**
     * Send the given message to the remote peer synchronously.
     *
     * @param message The message
     */
    @Override
    fun sendSync(message: Any, mediaType: MediaType? = MediaType.APPLICATION_JSON_TYPE) {
        try {
            sendAsync(message, mediaType).get()
        } catch (e: InterruptedException) {
            throw WebSocketSessionException("Send Interrupted")
        } catch (e: ExecutionException) {
            throw WebSocketSessionException("Send Failure: " + e.message, e)
        }
    }

    /**
     * Send the given message to the remote peer.
     * The resulting [Publisher] does not start sending until subscribed to.
     * If you return it from Micronaut annotated methods such as [io.micronaut.websocket.annotation.OnOpen] and [io.micronaut.websocket.annotation.OnMessage],
     * Micronaut will subscribe to it and send the message without blocking.
     *
     * @param message The message
     * @param <T> The message type
     * @return A [Publisher] that either emits an error or emits the message once it has been published successfully.
    </T> */
    fun <T> send(message: T): Publisher<T>? {
        return send(message, MediaType.APPLICATION_JSON_TYPE)
    }

    /**
     * Send the given message to the remote peer asynchronously.
     *
     * @param message The message
     * @param <T> The message type
     * @return A [CompletableFuture] that tracks the execution. [CompletableFuture.get] and related methods will return the message on success, on error throw the underlying Exception.
    </T> */
    fun <T> sendAsync(message: T): CompletableFuture<T> {
        return sendAsync(message, MediaType.APPLICATION_JSON_TYPE)
    }

    /**
     * The subprotocol if one is used.
     * @return The subprotocol
     */
    val subprotocol: Optional<String>
        get() = Optional.empty()

    /**
     * The request parameters used to create this session.
     *
     * @return The request parameters
     */
    val requestParameters: ConvertibleMultiValues<String?>
        get() = ConvertibleMultiValues.empty()

    /**
     * Any matching URI path variables.
     *
     * @return The path variables
     */
    val uriVariables: ConvertibleValues<Any?>
        get() = ConvertibleValues.empty()

    /**
     * The user [Principal] used to create the session.
     *
     * @return The [Principal]
     */
    val userPrincipal: Optional<Principal>
        get() = Optional.empty()

    override fun close()

    /**
     * Close the session with the given event.
     *
     * @param closeReason The close event
     */
    fun close(closeReason: CloseReason?)
}