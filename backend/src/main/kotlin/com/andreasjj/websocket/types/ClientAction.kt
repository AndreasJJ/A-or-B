package com.andreasjj.websocket.types

enum class ClientAction : Action {
    STARTGAME,
    ENDGAME,
    SKIPROUND,
    NEXTROUND,
    ANSWER,
    PONG
}