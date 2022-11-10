package com.orion.mediaplayercompose.utils

import android.os.Handler
import android.os.Looper

class TaskLooper(
    private val task: () -> Unit,
    private val delayMillis: Long
) {
    private val mainHandler = Handler(Looper.getMainLooper())

    private val repeatableTask: Runnable = object : Runnable {
        override fun run() {
            task.invoke()
            mainHandler.postDelayed(this, delayMillis)
        }
    }

    fun startTask() {
        mainHandler.postDelayed(repeatableTask, delayMillis)
    }

    fun stopTask() {
        mainHandler.removeCallbacks(repeatableTask)
    }


}