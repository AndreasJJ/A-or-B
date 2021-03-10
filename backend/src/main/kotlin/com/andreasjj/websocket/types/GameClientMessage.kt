package com.andreasjj.websocket.types

import com.andreasjj.websocket.types.ClientAction
import com.andreasjj.websocket.types.Message
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
data class GameClientMessage(
    @field:NotNull override var action: ClientAction,
    @field:NotBlank var text: String
): Message