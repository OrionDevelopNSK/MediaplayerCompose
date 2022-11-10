package com.orion.mediaplayercompose.models

import com.orion.mediaplayercompose.utils.PlaybackMode
import com.orion.mediaplayercompose.viewmodels.PlayerViewModel
import javax.inject.Inject

class StateModeChanger @Inject constructor(){

    private lateinit var viewModel: PlayerViewModel

    fun setPlayerViewModel(viewModel: PlayerViewModel) {
        this.viewModel = viewModel
    }

    fun changeStateMode() {
        viewModel.playbackMode.value = when (viewModel.playbackMode.value as PlaybackMode) {
            PlaybackMode.LOOP -> PlaybackMode.RANDOM
            PlaybackMode.RANDOM -> PlaybackMode.REPEAT
            PlaybackMode.REPEAT -> PlaybackMode.LOOP
        }
    }
}