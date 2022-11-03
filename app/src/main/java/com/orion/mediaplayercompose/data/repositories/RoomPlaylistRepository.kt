package com.orion.mediaplayercompose.data.repositories

import com.orion.mediaplayercompose.data.dao.RoomDao
import com.orion.mediaplayercompose.data.entities.PlaylistEntity
import com.orion.mediaplayercompose.data.entities.PlaylistSongEntity
import com.orion.mediaplayercompose.data.entities.SongEntity
import com.orion.mediaplayercompose.data.models.Playlist
import com.orion.mediaplayercompose.data.models.Song

class RoomPlaylistRepository(private val roomDao: RoomDao) {

    suspend fun insertOrUpdatePlaylistAndSoundTrack(playlistEntity: PlaylistEntity, playlistSongEntityList : List<PlaylistSongEntity>){
        roomDao.deleteByPlaylistNameFromLinkingTable(playlistEntity.playlistName)
        roomDao.insertPlaylistAndSoundTrack(playlistEntity, playlistSongEntityList)
    }

    suspend fun getPlaylistWithSoundTrack() : MutableMap<Playlist, MutableList<Song>>{
        return roomDao.getPlaylistWithSoundTrack()
    }

    suspend fun deletePlaylists(playlist: PlaylistEntity) {
        roomDao.deletePlaylist(playlist)
    }

    suspend fun insertAllSongs(songs: List<Song>) {
        val songsEntities = mutableListOf<SongEntity>()

        for (song in songs) {
            val songEntity = SongEntity(
                data = song.data,
                title = song.title,
                artist = song.artist,
                duration = song.duration,
                rating = song.rating,
                countOfLaunches = song.countOfLaunches
            )
            songsEntities.add(songEntity)
        }
        roomDao.insertAllSongs(songsEntities)
    }
}