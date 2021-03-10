package com.andreasjj.websocket.types

import com.andreasjj.websocket.types.Message
import com.andreasjj.websocket.types.ServerAction
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
data class GameServerMessage(
    @field:NotNull override var action: ServerAction,
    @field:NotBlank var text: String
): Message