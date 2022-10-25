package com.orion.mediaplayercompose.utils

import android.app.Activity
import android.database.ContentObserver
import android.os.Handler
import android.provider.MediaStore

class MediaScannerObserver(handler: Handler, activity: Activity) : ContentObserver(handler) {

    init {
        activity.contentResolver.registerContentObserver(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true, this
        )
    }

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        println()   //TODO
    }

}