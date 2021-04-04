package com.andreasjj.websocket.types

enum class ClientAction : Action {
    STARTGAME,
    JOINGAME,
    ENDGAME,
    SKIPROUND,
    NEXTROUND,
    ANSWER,
    PONG
}