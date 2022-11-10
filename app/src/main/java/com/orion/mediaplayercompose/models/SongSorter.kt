package com.orion.mediaplayercompose.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.orion.mediaplayercompose.utils.Sorting
import com.orion.mediaplayercompose.utils.SortingType
import com.orion.mediaplayercompose.viewmodels.PlayerViewModel
import javax.inject.Inject

class SongSorter @Inject constructor(){

    private lateinit var viewModel: PlayerViewModel

    fun setPlayerViewModel(viewModel: PlayerViewModel) {
        this.viewModel = viewModel
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun changeSortingType(sorting: SortingType) {
        sortSongs(sorting)
    }

    fun sortSongs(sorting: SortingType? = viewModel.sortingType.value) {
        viewModel.allSongs.value =
            when (sorting) {
                SortingType.DATE -> viewModel.defaultSongList.value?.let { Sorting.byDate(it) }
                SortingType.RATING -> viewModel.defaultSongList.value?.let { Sorting.byRating(it) }
                SortingType.REPEATABILITY -> viewModel.defaultSongList.value?.let { Sorting.byRepeatability(it)}
                else -> viewModel.defaultSongList.value?.let { Sorting.byDate(it) }
            }
        viewModel.sortingType.value = sorting
    }

}