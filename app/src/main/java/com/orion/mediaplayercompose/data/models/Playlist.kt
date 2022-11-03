package com.orion.mediaplayercompose.data.models

import com.orion.mediaplayercompose.data.entities.PlaylistEntity
import com.orion.mediaplayercompose.data.entities.SongEntity

data class Playlist(
    val name: String,
    val songs: MutableList<Song>
){
    fun toPlaylistEntity(): PlaylistEntity {
        val songEntities = mutableListOf<SongEntity>()

        for (song in songs) {
            songEntities.add(song.toSongEntity())
        }
        val playlistEntity = PlaylistEntity(playlistName = name)
        playlistEntity.songEntities = songEntities
        return playlistEntity
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Playlist

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }


}
