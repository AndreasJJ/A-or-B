package com.andreasjj.websocket

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
data class GameServerMessage(
    @field:NotNull var type: GameServerMessageType,
    @field:NotBlank var text: String
)

enum class GameServerMessageType {
    WELCOME,
    END,
    NEWROUND,
    RESULT,
    ERROR,
    PING
}