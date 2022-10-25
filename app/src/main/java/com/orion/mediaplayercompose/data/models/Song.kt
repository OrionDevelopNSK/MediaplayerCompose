package com.orion.mediaplayercompose.data.models

import com.orion.mediaplayercompose.data.entities.SongEntity

data class Song(
    val data: String,
    val title: String,
    val artist: String,
    val duration: Int,
    var rating: Int,
    var countOfLaunches: Int
) {
    fun toSongEntity(): SongEntity {
        return SongEntity(
            data = data,
            title = title,
            artist = artist,
            duration = duration,
            rating = rating,
            countOfLaunches = countOfLaunches
        )
    }

}
