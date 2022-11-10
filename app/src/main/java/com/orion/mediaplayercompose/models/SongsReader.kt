package com.orion.mediaplayercompose.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.orion.mediaplayercompose.data.database.DatabaseHelper
import com.orion.mediaplayercompose.viewmodels.PlayerViewModel
import javax.inject.Inject

class SongsReader @Inject constructor(
    private val databaseHelper: DatabaseHelper
){
    private lateinit var viewModel: PlayerViewModel

    fun setPlayerViewModel(viewModel: PlayerViewModel) {
        this.viewModel = viewModel
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun readSongs() {
        viewModel.defaultSongList.value = databaseHelper.readSongs()
    }
}