package com.orion.mediaplayercompose.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orion.mediaplayercompose.data.models.Playlist
import com.orion.mediaplayercompose.data.models.Song
import com.orion.mediaplayercompose.utils.Sorting
import com.orion.mediaplayercompose.utils.SortingType
import com.orion.mediaplayercompose.utils.StateMode

class PlayerViewModel : ViewModel() {

    val playlists: MutableLiveData<Map<Playlist, MutableList<Song>>> = MutableLiveData()
    val songs: MutableLiveData<List<Song>> = MutableLiveData()
    val currentPlaylist: MutableLiveData<Playlist> = MutableLiveData()
    val playerPlayed: MutableLiveData<Boolean> = MutableLiveData(false)
    val sortingType: MutableLiveData<SortingType> = MutableLiveData(SortingType.DATE)
    val stateMode: MutableLiveData<StateMode> = MutableLiveData(StateMode.LOOP)

    lateinit var defaultSongList: List<Song>

    fun savePlaylist(states: MutableList<Boolean>) {
        states.forEach { println(it) }
        println("Click")
    }

    fun saveSongs(s: List<Song>) {
        defaultSongList = ArrayList(s)
        when (sortingType.value as SortingType) {
            SortingType.DATE -> songs.postValue(Sorting.byDate(s))
            SortingType.RATING -> songs.postValue(Sorting.byRating(s))
            SortingType.REPEATABILITY -> songs.postValue(Sorting.byRepeatability(s))
        }
    }

    fun changeSortingType(sorting: SortingType) {
        when (sorting) {
            SortingType.DATE -> songs.postValue(Sorting.byDate(defaultSongList))
            SortingType.RATING -> songs.postValue(Sorting.byRating(defaultSongList))
            SortingType.REPEATABILITY -> songs.postValue(Sorting.byRepeatability(defaultSongList))
        }
        sortingType.value = sorting
    }

    fun changeStateMode() {
        stateMode.value = when (stateMode.value as StateMode) {
            StateMode.LOOP -> StateMode.RANDOM
            StateMode.RANDOM -> StateMode.REPEAT
            StateMode.REPEAT -> StateMode.LOOP
        }
    }
}