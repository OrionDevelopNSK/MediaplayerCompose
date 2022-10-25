package com.orion.mediaplayercompose.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.IBinder

class MediaSessionService : Service() {

    private var isScreenOn : Boolean = true

    inner class NoisyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent.action) {
                //TODO
            }
        }
    }


    inner class ScreenBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (Intent.ACTION_SCREEN_OFF == intent.action) {
                isScreenOn = false
            } else if (Intent.ACTION_SCREEN_ON == intent.action) {
                isScreenOn = true
                //TODO
            }
        }
    }



    override fun onBind(p0: Intent): IBinder {
        TODO("Not yet implemented")
    }
}