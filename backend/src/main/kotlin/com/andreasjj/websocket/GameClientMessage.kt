package com.andreasjj.websocket

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
data class GameClientMessage(
    @field:NotNull var type: GameClientMessageType,
    @field:NotBlank var text: String
)

enum class GameClientMessageType {
    STARTGAME,
    ENDGAME,
    SKIPROUND,
    NEXTROUND,
    ANSWER,
    PONG
}