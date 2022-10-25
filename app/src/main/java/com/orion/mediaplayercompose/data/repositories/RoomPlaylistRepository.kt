package com.orion.mediaplayercompose.data.repositories

import com.orion.mediaplayercompose.data.dao.PlaylistDao
import com.orion.mediaplayercompose.data.entities.PlaylistEntity
import com.orion.mediaplayercompose.data.entities.PlaylistSongEntity
import com.orion.mediaplayercompose.data.models.Playlist
import com.orion.mediaplayercompose.data.models.Song

class RoomPlaylistRepository(private val playlistDao: PlaylistDao) {

    fun insertOrUpdatePlaylistAndSoundTrack(playlistEntity: PlaylistEntity, playlistSongEntityList : List<PlaylistSongEntity>){
        playlistDao.deleteByPlaylistNameFromLinkingTable(playlistEntity.playlistName)
        playlistDao.insertPlaylistAndSoundTrack(playlistEntity, playlistSongEntityList)
    }

    fun getPlaylistWithSoundTrack() : Map<Playlist, MutableList<Song>>{
        return playlistDao.getPlaylistWithSoundTrack()
    }

    fun deletePlaylists(playlist: PlaylistEntity) {
        playlistDao.deletePlaylist(playlist)
    }
}