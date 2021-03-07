package com.andreasjj.jobs

import com.andreasjj.entity.RoundId
import io.micronaut.websocket.WebSocketBroadcaster

class NextRoundTask(/*round: RoundId, */private val broadcaster: WebSocketBroadcaster, private val duration: Long) : Runnable {
    //private val emailUseCase: EmailUseCase

    override fun run() {
        //emailUseCase.send(email, message)
        broadcaster.broadcast("hi")
    }

    init {
        //this.emailUseCase = emailUseCase
    }
}