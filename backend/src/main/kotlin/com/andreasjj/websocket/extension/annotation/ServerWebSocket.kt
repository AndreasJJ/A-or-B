package com.andreasjj.websocket.extension.annotation

import io.micronaut.core.util.StringUtils;
import javax.inject.Singleton
import io.micronaut.context.annotation.DefaultScope
import io.micronaut.websocket.WebSocketVersion;



@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@WebSocketComponent
@DefaultScope(Singleton::class)
annotation class ServerWebSocket(
    /**
     * @return The URI of the action
     */
    val value: String = DEFAULT_URI,
    /**
     * @return The URI of the action
     */
    val uri: String = DEFAULT_URI,
    /**
     * @return The WebSocket version to use to connect
     */
    val version: WebSocketVersion = WebSocketVersion.V13,
    /**
     * @return A csv of the supported subprotocols
     */
    val subprotocols: String = StringUtils.EMPTY_STRING
) {
    companion object {
        /**
         * The default WebSocket URI.
         */
        const val DEFAULT_URI = "/ws"
    }
}