package com.andreasjj.jobs

import com.andreasjj.entity.RoundId
import io.micronaut.scheduling.TaskScheduler
import io.micronaut.websocket.WebSocketBroadcaster
import javax.inject.Inject
import java.time.Duration;
import java.util.concurrent.ScheduledFuture

class NextRoundJob() {
    @Inject
    lateinit var taskScheduler: TaskScheduler

    private var scheduledTasks: MutableMap<RoundId, ScheduledFuture<*>> = mutableMapOf()

    fun register(broadcaster: WebSocketBroadcaster, roundId: RoundId, duration: Long) {
        scheduleJob(broadcaster, roundId, duration)
    }

    fun cancel(roundId: RoundId) {
        cancelJob(roundId)
    }

    private fun cancelJob(roundId: RoundId) {
        scheduledTasks[roundId]?.cancel(false)
    }

    private fun scheduleJob(broadcaster: WebSocketBroadcaster, roundId: RoundId, durationInSeconds: Long) {
        val duration: Duration = Duration.ofSeconds(durationInSeconds)
        val task: Runnable = NextRoundTask(broadcaster, 30)
        var future = taskScheduler.schedule(duration, task)
        scheduledTasks[roundId] = future
    }
}