package com.andreasjj.websocket.annotation

import com.andreasjj.websocket.types.ClientAction
import io.micronaut.context.annotation.Executable
import io.micronaut.security.rules.SecurityRule

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@Executable
annotation class OnAction(
    val action: ClientAction,
    val args: Array<String> = [],
    val auth: String = SecurityRule.IS_ANONYMOUS
)