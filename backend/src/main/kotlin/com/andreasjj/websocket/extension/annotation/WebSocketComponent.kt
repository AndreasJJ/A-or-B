package com.andreasjj.websocket.extension.annotation

import io.micronaut.websocket.WebSocketVersion;


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class WebSocketComponent(
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
    val version: WebSocketVersion = WebSocketVersion.V13
) {
    companion object {
        /**
         * The default WebSocket URI.
         */
        const val DEFAULT_URI = "/ws"
    }
}
