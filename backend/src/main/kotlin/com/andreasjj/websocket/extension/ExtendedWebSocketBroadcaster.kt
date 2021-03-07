package com.andreasjj.websocket.extension

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Replaces
import io.micronaut.websocket.WebSocketBroadcaster
import io.micronaut.http.MediaType
import io.micronaut.websocket.exceptions.WebSocketSessionException
import java.util.concurrent.ExecutionException
import java.util.concurrent.CompletableFuture
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import java.util.*
import java.util.function.Predicate

@Replaces(WebSocketBroadcaster::class)
interface ExtendedWebSocketBroadcaster {
    /**
     * When used on the server this method will broadcast a message to all open WebSocket connections that match the given filter.
     * The resulting [Publisher] does not start sending until subscribed to.
     * If you return it from Micronaut annotated methods such as [io.micronaut.websocket.annotation.OnOpen] and [io.micronaut.websocket.annotation.OnMessage],
     * Micronaut will subscribe to it and send the message without blocking.
     *
     * @param message The message
     * @param mediaType The media type of the message. Used to lookup an appropriate codec via the [io.micronaut.http.codec.MediaTypeCodecRegistry].
     * @param filter The filter to apply
     * @param <T> The message type
     * @return A [Publisher] that either emits an error or emits the message once it has been published successfully.
    </T> */
    fun <T> broadcast(message: T, mediaType: MediaType?, filter: Predicate<ExtendedWebSocketSession>): Publisher<T>?

    /**
     * When used on the server this method will broadcast a message to all open WebSocket connections.
     * The resulting [Publisher] does not start sending until subscribed to.
     * If you return it from Micronaut annotated methods such as [io.micronaut.websocket.annotation.OnOpen] and [io.micronaut.websocket.annotation.OnMessage],
     * Micronaut will subscribe to it and send the message without blocking.
     *
     * @param message The message
     * @param mediaType The media type of the message. Used to lookup an appropriate codec via the [io.micronaut.http.codec.MediaTypeCodecRegistry].
     * @param <T> The message type
     * @return A [Publisher] that either emits an error or emits the message once it has been published successfully.
    </T> */
    fun <T> broadcast(message: T, mediaType: MediaType?): Publisher<T>? {
        return broadcast(message, mediaType, Predicate<ExtendedWebSocketSession> { s -> true })
    }

    /**
     * When used on the server this method will broadcast a message to all open WebSocket connections.
     * The resulting [Publisher] does not start sending until subscribed to.
     * If you return it from Micronaut annotated methods such as [io.micronaut.websocket.annotation.OnOpen] and [io.micronaut.websocket.annotation.OnMessage],
     * Micronaut will subscribe to it and send the message without blocking.
     *
     * @param message The message
     * @param <T> The message type
     * @return A [Publisher] that either emits an error or emits the message once it has been published successfully.
    </T> */
    fun <T> broadcast(message: T): Publisher<T>? {
        return broadcast(message, MediaType.APPLICATION_JSON_TYPE,
            Predicate<ExtendedWebSocketSession> { s -> true })
    }

    /**
     * When used on the server this method will broadcast a message to all open WebSocket connections that match the given filter.
     * The resulting [Publisher] does not start sending until subscribed to.
     * If you return it from Micronaut annotated methods such as [io.micronaut.websocket.annotation.OnOpen] and [io.micronaut.websocket.annotation.OnMessage],
     * Micronaut will subscribe to it and send the message without blocking.
     *
     * @param message The message
     * @param filter The filter to apply
     * @param <T> The message type
     * @return A [Publisher] that either emits an error or emits the message once it has been published successfully.
    </T> */
    fun <T> broadcast(message: T, filter: Predicate<ExtendedWebSocketSession>): Publisher<T>? {
        Objects.requireNonNull(filter, "The filter cannot be null")
        return broadcast(message, MediaType.APPLICATION_JSON_TYPE, filter)
    }

    /**
     * When used on the server this method will broadcast a message to all open WebSocket connections.
     *
     * @param message The message
     * @param mediaType The media type of the message. Used to lookup an appropriate codec via the [io.micronaut.http.codec.MediaTypeCodecRegistry].
     * @param filter The filter
     * @param <T> The message type
     * @return A [CompletableFuture] that tracks the execution. [CompletableFuture.get] and related methods will return the message on success, on error throw the underlying Exception.
    </T> */
    fun <T> broadcastAsync(
        message: T,
        mediaType: MediaType?,
        filter: Predicate<ExtendedWebSocketSession>
    ): CompletableFuture<T> {
        val future = CompletableFuture<T>()
        Flowable.fromPublisher(broadcast(message, mediaType, filter)).subscribe(
            { o: T -> },
            { ex: Throwable? -> future.completeExceptionally(ex) }
        ) { future.complete(message) }
        return future
    }

    /**
     * When used on the server this method will broadcast a message to all open WebSocket connections.
     *
     * @param message The message
     * @param <T> The message type
     * @return A [CompletableFuture] that tracks the execution. [CompletableFuture.get] and related methods will return the message on success, on error throw the underlying Exception.
    </T> */
    fun <T> broadcastAsync(message: T): CompletableFuture<T>? {
        return broadcastAsync(message, MediaType.APPLICATION_JSON_TYPE,
            Predicate<ExtendedWebSocketSession> { o -> true })
    }

    /**
     * When used on the server this method will broadcast a message to all open WebSocket connections that match the given filter.
     *
     * @param message The message
     * @param filter The filter to apply
     * @param <T> The message type
     * @return A [CompletableFuture] that tracks the execution. [CompletableFuture.get] and related methods will return the message on success, on error throw the underlying Exception.
    </T> */
    fun <T> broadcastAsync(message: T, filter: Predicate<ExtendedWebSocketSession>): CompletableFuture<T>? {
        return broadcastAsync(message, MediaType.APPLICATION_JSON_TYPE, filter)
    }

    /**
     * When used on the server this method will broadcast a message to all open WebSocket connections.
     *
     * @param message The message
     * @param mediaType The media type of the message. Used to lookup an appropriate codec via the [io.micronaut.http.codec.MediaTypeCodecRegistry].
     * @param <T> The message type
     * @return A [CompletableFuture] that tracks the execution. [CompletableFuture.get] and related methods will return the message on success, on error throw the underlying Exception.
    </T> */
    fun <T> broadcastAsync(message: T, mediaType: MediaType?): CompletableFuture<T>? {
        return broadcastAsync(message, mediaType, Predicate<ExtendedWebSocketSession> { o -> true })
    }

    /**
     * When used on the server this method will broadcast a message to all open WebSocket connections.
     *
     * @param message The message
     * @param mediaType The media type of the message. Used to lookup an appropriate codec via the [io.micronaut.http.codec.MediaTypeCodecRegistry].
     * @param filter The filter
     * @param <T> The message type
    </T> */
    fun <T> broadcastSync(message: T, mediaType: MediaType?, filter: Predicate<ExtendedWebSocketSession>) {
        try {
            broadcastAsync(message, mediaType, filter).get()
        } catch (e: InterruptedException) {
            throw WebSocketSessionException("Broadcast Interrupted")
        } catch (e: ExecutionException) {
            throw WebSocketSessionException("Broadcast Failure: " + e.message, e)
        }
    }

    /**
     * When used on the server this method will broadcast a message to all open WebSocket connections.
     *
     * @param message The message
     * @param <T> The message type
    </T> */
    fun <T> broadcastSync(message: T) {
        broadcastSync(message, MediaType.APPLICATION_JSON_TYPE,
            Predicate<ExtendedWebSocketSession> { o -> true })
    }

    /**
     * When used on the server this method will broadcast a message to all open WebSocket connections that match the given filter.
     *
     * @param message The message
     * @param filter The filter to apply
     * @param <T> The message type
    </T> */
    fun <T> broadcastSync(message: T, filter: Predicate<ExtendedWebSocketSession>) {
        broadcastSync(message, MediaType.APPLICATION_JSON_TYPE, filter)
    }

    /**
     * When used on the server this method will broadcast a message to all open WebSocket connections.
     *
     * @param message The message
     * @param mediaType The media type of the message. Used to lookup an appropriate codec via the [io.micronaut.http.codec.MediaTypeCodecRegistry].
     * @param <T> The message type
    </T> */
    fun <T> broadcastSync(message: T, mediaType: MediaType?) {
        broadcastSync(message, mediaType, Predicate<ExtendedWebSocketSession> { o -> true })
    }
}