package com.orion.mediaplayercompose.data.database

import android.content.ContentResolver
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.orion.mediaplayercompose.data.models.Song

class AudioReader(private val context: Context) {

    @RequiresApi(Build.VERSION_CODES.R)
    fun readMediaData(): List<Song> {
        val projection = arrayOf(
            MediaStore.MediaColumns.TITLE,
            MediaStore.MediaColumns.ARTIST,
            MediaStore.MediaColumns.DURATION,
            MediaStore.MediaColumns.DATA
        )

        val contentResolver: ContentResolver = context.applicationContext.contentResolver
        val cursorAudio = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            "${MediaStore.MediaColumns.DATA} like ? OR ${MediaStore.MediaColumns.DATA} like ?",
            arrayOf("%mp3", "%wav"),
            null
        )
        cursorAudio!!.moveToFirst()

        val columnIndexTitle = cursorAudio.getColumnIndex(MediaStore.MediaColumns.TITLE)
        val columnIndexArtist = cursorAudio.getColumnIndex(MediaStore.MediaColumns.ARTIST)
        val columnIndexDuration = cursorAudio.getColumnIndex(MediaStore.MediaColumns.DURATION)
        val columnIndexData = cursorAudio.getColumnIndex(MediaStore.MediaColumns.DATA)

        val songs = mutableListOf<Song>()

        while (cursorAudio.moveToNext()) {
            val song = Song(
                data = cursorAudio.getString(columnIndexData),
                title = cursorAudio.getString(columnIndexTitle),
                artist = cursorAudio.getString(columnIndexArtist),
                duration = cursorAudio.getInt(columnIndexDuration),
                rating = 0,
                countOfLaunches = 0
            )
            songs.add(song)
        }
        return songs
    }
}