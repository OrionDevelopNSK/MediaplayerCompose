package com.orion.mediaplayercompose.data.models

import com.orion.mediaplayercompose.data.entities.PlaylistEntity
import com.orion.mediaplayercompose.data.entities.SongEntity

data class Playlist(
    val name: String,
    val songs: List<Song>
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
}
