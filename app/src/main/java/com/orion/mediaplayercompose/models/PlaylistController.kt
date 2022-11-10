package com.orion.mediaplayercompose.models

import com.orion.mediaplayercompose.data.database.DatabaseHelper
import com.orion.mediaplayercompose.data.models.Playlist
import com.orion.mediaplayercompose.data.models.Song
import com.orion.mediaplayercompose.viewmodels.PlayerViewModel
import javax.inject.Inject

class PlaylistController @Inject constructor(
    private val databaseHelper: DatabaseHelper
){

    private lateinit var viewModel: PlayerViewModel

    fun setPlayerViewModel(viewModel: PlayerViewModel) {
        this.viewModel = viewModel
    }

    fun createPlaylist(states: MutableList<Boolean>) {
        val tmpSongs: MutableList<Song> = mutableListOf()
        states.forEachIndexed { index, state -> if (state) viewModel.allSongs.value?.get(index)
            ?.let { tmpSongs.add(it) } }
        viewModel.lastCreatedPlaylist.value = viewModel.playlistName.value?.let { Playlist(it, tmpSongs) }
    }

    fun savePlaylist() {
        val chosenPlaylist = viewModel.lastCreatedPlaylist.value
        val playlists = viewModel.allPlaylists.value
        if (playlists?.keys?.contains(chosenPlaylist) == true) playlists.remove(chosenPlaylist)
        chosenPlaylist?.let { viewModel.allPlaylists.value?.put(it, chosenPlaylist.songs) }
        databaseHelper.savePlaylist(chosenPlaylist)
    }

    fun loadPlaylist() {
        viewModel.allPlaylists.value = databaseHelper.loadPlaylist()
    }

    fun deletePlaylist(currentPlaylist: Playlist) {
        viewModel.allPlaylists.value = databaseHelper.deletePlaylist(currentPlaylist)
    }
}