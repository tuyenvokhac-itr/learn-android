package com.example.composablelearning.blecore.utils

import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object ThreadPoolCreater {
    private const val MIN_NUMBER_OF_CORES = 6
    private const val MAX_NUMBER_OF_CORES = 6

    // Set the amount of time an idle thread waits before terminating
    private const val KEEP_ALIVE_TIME = 1L

    // Sets the Time Unit to seconds
    private val KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS

    fun create(): ThreadPoolExecutor {
        return ThreadPoolExecutor(
            MIN_NUMBER_OF_CORES,
            MAX_NUMBER_OF_CORES,
            KEEP_ALIVE_TIME,
            KEEP_ALIVE_TIME_UNIT,
            LinkedBlockingDeque<Runnable>(),
            HandlerReject()
        )
    }
}

class HandlerReject : RejectedExecutionHandler {
    override fun rejectedExecution(r: Runnable?, executor: ThreadPoolExecutor?) {
        try {
            executor?.maximumPoolSize = (executor?.maximumPoolSize ?: 0) + 1
            executor?.submit(r)
        } catch (_: Exception) {
        }
    }
}