package com.andreasjj.websocket.annotation

import com.andreasjj.websocket.types.ClientAction
import io.micronaut.context.annotation.Executable

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@Executable
annotation class OnAction(
    val action: ClientAction,
)
