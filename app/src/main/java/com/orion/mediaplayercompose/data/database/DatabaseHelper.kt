package com.orion.mediaplayercompose.data.database

import android.os.Build
import androidx.annotation.RequiresApi
import com.orion.mediaplayercompose.data.entities.PlaylistSongEntity
import com.orion.mediaplayercompose.data.models.Playlist
import com.orion.mediaplayercompose.data.models.Song
import com.orion.mediaplayercompose.data.repositories.RoomPlaylistRepository
import com.orion.mediaplayercompose.utils.Sorting
import com.orion.mediaplayercompose.utils.SortingType
import com.orion.mediaplayercompose.utils.SortingType.*
import com.orion.mediaplayercompose.viewmodels.PlayerViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class DatabaseHelper @Inject constructor(
    database: AppDataBase,
    private val audioReader: AudioReader
) {
    private val roomPlaylistRepository = RoomPlaylistRepository(database.roomDao())


    @RequiresApi(Build.VERSION_CODES.R)
    fun readSongs() : MutableList<Song> {
        var defaultSongList: MutableList<Song> = emptyArray<Song>().toMutableList()
        runBlocking {
            val job = async {
                val readMediaData = audioReader.readMediaData()
                roomPlaylistRepository.insertAllSongs(readMediaData)
                defaultSongList = ArrayList(readMediaData)
            }
            job.await()
        }
        return defaultSongList
    }

    fun savePlaylist(chosenPlaylist: Playlist?) {
        runBlocking {
            launch {
                val playlistEntity = chosenPlaylist?.toPlaylistEntity()
                val songEntities = playlistEntity?.songEntities
                val playlistSongEntities = mutableListOf<PlaylistSongEntity>()
                for (songEntity in songEntities!!) {
                    playlistSongEntities.add(
                        PlaylistSongEntity(playlistEntity.playlistName, songEntity.data)
                    )
                }
                roomPlaylistRepository.insertOrUpdatePlaylistAndSoundTrack(
                    playlistEntity,
                    playlistSongEntities
                )
            }
        }
    }

    fun loadPlaylist(): MutableMap<Playlist, MutableList<Song>> {
        val playlists: MutableMap<Playlist, MutableList<Song>>
        runBlocking {
            val job = async { roomPlaylistRepository.getPlaylistWithSoundTrack() }
            playlists = job.await()
        }
        return playlists;
    }

    fun deletePlaylist(currentPlaylist: Playlist): MutableMap<Playlist, MutableList<Song>> {
        val playlists: MutableMap<Playlist, MutableList<Song>>
        runBlocking {
            val job = async {
                roomPlaylistRepository.deletePlaylists(currentPlaylist.toPlaylistEntity())
                roomPlaylistRepository.getPlaylistWithSoundTrack()
            }
            playlists = job.await()
        }
        return playlists
    }
}