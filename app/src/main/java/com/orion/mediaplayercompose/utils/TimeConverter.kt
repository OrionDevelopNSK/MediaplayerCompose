package com.orion.mediaplayercompose.utils

import kotlin.math.roundToInt

class TimeConverter {
    companion object{
        fun toMinutesAndSeconds(duration : Long) : String{
            val min: Int = (duration / (60 * 1000)).toInt()
            val sec = ((duration - min * 60 * 1000) / 1000f).roundToInt()
            return String.format("%02d", min) + ":" + String.format("%02d", sec)
        }
    }
}