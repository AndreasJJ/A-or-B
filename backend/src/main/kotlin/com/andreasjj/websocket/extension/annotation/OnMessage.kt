package com.andreasjj.websocket.extension.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@WebSocketMapping
annotation class OnMessage(
    /**
     * The maximum size of a WebSocket payload.
     * @return The max size
     */
    val maxPayloadLength: Int = 65536
)
