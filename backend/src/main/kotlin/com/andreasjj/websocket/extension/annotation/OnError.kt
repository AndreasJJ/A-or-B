package com.andreasjj.websocket.extension.annotation

import java.lang.annotation.ElementType


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@WebSocketMapping
annotation class OnError
