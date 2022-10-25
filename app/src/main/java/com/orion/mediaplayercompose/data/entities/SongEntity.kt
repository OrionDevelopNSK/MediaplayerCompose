package com.orion.mediaplayercompose.data.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.orion.mediaplayercompose.data.models.Song

@Entity(
    tableName = "songs",
    indices = [
        Index(
            value = ["data"],
            unique = true
        )]
)
data class SongEntity(
    @PrimaryKey
    @NonNull
    val data: String,
    val title: String,
    val artist: String,
    val duration: Int,
    var rating: Int,
    var countOfLaunches: Int
) {
    fun toSong(): Song {
        return Song(
            data = data,
            title = title,
            artist = artist,
            duration = duration,
            rating = rating,
            countOfLaunches = countOfLaunches
        )
    }
}