package com.orion.mediaplayercompose.models

import com.orion.mediaplayercompose.data.models.Playlist
import com.orion.mediaplayercompose.viewmodels.PlayerViewModel
import javax.inject.Inject

class ChosenSongListController @Inject constructor() {

    private lateinit var viewModel: PlayerViewModel

    fun setPlayerViewModel(viewModel: PlayerViewModel) {
        this.viewModel = viewModel
    }

    fun createChosenSongList(playlist: Playlist) {
        viewModel.lastCreatedPlaylist.value = playlist
        val chosenSongListFromUi = viewModel.allSongs.value?.size?.let { MutableList(it) { false } }
        val songsOfPlaylist = playlist.songs
        songsOfPlaylist.forEach {
            val indexOf = viewModel.allSongs.value?.indexOf(it)
            indexOf?.let { it1 -> chosenSongListFromUi?.add(it1, true) }
        }
        viewModel.isChosenSongListFromUi.value = chosenSongListFromUi
    }

    fun clearChosenSongList() {
        if (viewModel.isChosenSongListFromUi.value?.isNotEmpty() == true) viewModel.isChosenSongListFromUi.value?.clear()
        viewModel.isChosenSongListFromUi.value = viewModel.allSongs.value?.size?.let { MutableList(it) { false } }
    }
}