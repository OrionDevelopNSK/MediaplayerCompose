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
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class DatabaseHelper @Inject constructor(
    database: AppDataBase,
    private val audioReader: AudioReader
) {
    lateinit var defaultSongList: List<Song>
    lateinit var viewModel: PlayerViewModel

    private val roomPlaylistRepository = RoomPlaylistRepository(database.roomDao())

    @RequiresApi(Build.VERSION_CODES.R)
    fun readSongs() {
        runBlocking {
            launch {
                val readMediaData = audioReader.readMediaData()
                defaultSongList = ArrayList(readMediaData)
                sortSongs(viewModel.sortingType.value!!)
                roomPlaylistRepository.insertAllSongs(readMediaData)
            }
        }
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

    fun loadPlaylist() {


        runBlocking {
            launch {
                viewModel.allPlaylists.postValue(roomPlaylistRepository.getPlaylistWithSoundTrack())
//                AsyncTask.execute { viewModel.allPlaylists.postValue(roomPlaylistRepository.getPlaylistWithSoundTrack()) }
            }
        }
    }

    fun deletePlaylist(currentPlaylist: Playlist) =
        runBlocking {
            launch {
                roomPlaylistRepository.deletePlaylists(currentPlaylist.toPlaylistEntity())
                viewModel.allPlaylists.postValue(roomPlaylistRepository.getPlaylistWithSoundTrack())
            }
        }


    fun sortSongs(sorting: SortingType) {
        viewModel.allSongs.postValue(
            when (sorting) {
                DATE -> Sorting.byDate(defaultSongList)
                RATING -> Sorting.byRating(defaultSongList)
                REPEATABILITY -> Sorting.byRepeatability(defaultSongList)
            }
        )
        viewModel.sortingType.value = sorting
    }
}