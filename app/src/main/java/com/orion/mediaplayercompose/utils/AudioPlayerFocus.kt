package com.orion.mediaplayercompose.utils

import android.content.Context
import android.media.AudioFocusRequest
import android.media.AudioManager

class AudioPlayerFocus(private val context: Context) : AudioManager.OnAudioFocusChangeListener {

    private val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val audioFocusRequest: AudioFocusRequest =
        AudioFocusRequest
            .Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setOnAudioFocusChangeListener(this)
            .build()

    fun loseAudioFocus() {
        audioManager.abandonAudioFocusRequest(audioFocusRequest)
    }

    fun gainAudioFocus() {
        audioManager.requestAudioFocus(audioFocusRequest)
    }


    override fun onAudioFocusChange(state: Int) {
        TODO("Not yet implemented")

    }
}