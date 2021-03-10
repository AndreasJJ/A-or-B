package com.andreasjj.websocket.types

enum class ServerAction : Action {
    WELCOME,
    END,
    NEWROUND,
    RESULT,
    ERROR,
    PING,
    CONFIRMATION
}