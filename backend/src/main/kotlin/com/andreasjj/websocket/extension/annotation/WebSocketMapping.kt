package com.andreasjj.websocket.extension.annotation

import io.micronaut.context.annotation.Executable

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Executable
annotation class WebSocketMapping
