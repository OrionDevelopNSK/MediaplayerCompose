package com.orion.mediaplayercompose.data.database

import android.app.Application
import com.orion.mediaplayercompose.data.entities.PlaylistEntity
import com.orion.mediaplayercompose.data.entities.PlaylistSongEntity
import com.orion.mediaplayercompose.data.models.Playlist
import com.orion.mediaplayercompose.data.repositories.RoomPlaylistRepository
import com.orion.mediaplayercompose.viewmodels.PlayerViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PlaylistDatabaseHelper(
    private val application: Application,
    private val playerViewModel: PlayerViewModel
) {

    fun insertOrUpdatePlaylist(playlist: Playlist) = runBlocking {
        launch {
            val database = AppDataBase.getDatabase(application)
            val roomPlaylistRepository = RoomPlaylistRepository(database.playlistDao())
            val playlistEntity = playlist.toPlaylistEntity()
            val playlistSongEntityList = createPlaylistSoundtrackDbEntityList(playlistEntity)
            roomPlaylistRepository.insertOrUpdatePlaylistAndSoundTrack(
                playlistEntity,
                playlistSongEntityList
            )
            playerViewModel.playlists.postValue(roomPlaylistRepository.getPlaylistWithSoundTrack())
        }
    }

    private fun createPlaylistSoundtrackDbEntityList(playlistEntity: PlaylistEntity): List<PlaylistSongEntity> {
        val songEntities = playlistEntity.songEntities
        val playlistSongEntities = mutableListOf<PlaylistSongEntity>()
        for (songEntity in songEntities!!) {
            val playlistSongEntity = PlaylistSongEntity(
                playlistName = playlistEntity.playlistName,
                data = songEntity.data
            )
            playlistSongEntities.add(playlistSongEntity)
        }
        return playlistSongEntities
    }

    fun loadPlaylistWithSoundtrack() = runBlocking{
        launch {
            val database = AppDataBase.getDatabase(application)
            val roomPlaylistRepository = RoomPlaylistRepository(database.playlistDao())
            val playlists = roomPlaylistRepository.getPlaylistWithSoundTrack()
            playerViewModel.playlists.postValue(playlists)
        }
    }

    fun deletePlaylist(playlist: Playlist) = runBlocking {
        launch {
            val database = AppDataBase.getDatabase(application)
            val roomPlaylistRepository = RoomPlaylistRepository(database.playlistDao())
            roomPlaylistRepository.deletePlaylists(playlist.toPlaylistEntity())
            playerViewModel.playlists.postValue(roomPlaylistRepository.getPlaylistWithSoundTrack())
        }
    }

}