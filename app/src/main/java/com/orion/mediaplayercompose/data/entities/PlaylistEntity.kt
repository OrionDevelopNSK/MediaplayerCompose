package com.orion.mediaplayercompose.data.entities

import android.annotation.SuppressLint
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.orion.mediaplayercompose.data.models.Playlist
import com.orion.mediaplayercompose.data.models.Song

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @SuppressLint("KotlinNullnessAnnotation")
    @PrimaryKey
    @NonNull
    val playlistName: String,

    ) {

    @Ignore
    var songEntities: List<SongEntity>? = null

    fun toPlaylist(): Playlist {
        val songs = mutableListOf<Song>()

        for (songEntity in songEntities!!) {
            songs.add(songEntity.toSong())
        }

        return Playlist(name = playlistName, songs = songs)
    }
}