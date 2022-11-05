package com.orion.mediaplayercompose.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orion.mediaplayercompose.data.database.DatabaseHelper
import com.orion.mediaplayercompose.data.models.Playlist
import com.orion.mediaplayercompose.data.models.Song
import com.orion.mediaplayercompose.utils.PlaybackMode
import com.orion.mediaplayercompose.utils.Sorting
import com.orion.mediaplayercompose.utils.SortingType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val databaseHelper: DatabaseHelper
) : ViewModel() {

    val allPlaylists: MutableLiveData<MutableMap<Playlist, MutableList<Song>>> = MutableLiveData()
    val allSongs: MutableLiveData<List<Song>> = MutableLiveData(mutableListOf())
    val isMediaPlayerPlayed: MutableLiveData<Boolean> = MutableLiveData(false)
    val playbackMode: MutableLiveData<PlaybackMode> = MutableLiveData(PlaybackMode.LOOP)
    val currentPlaylist: MutableLiveData<Playlist> = MutableLiveData()
    val playlistName: MutableLiveData<String> = MutableLiveData("")
    val isChosenSongListFromUi: MutableLiveData<MutableList<Boolean>> =
        MutableLiveData(mutableListOf())

    val currentTabPosition : MutableLiveData<Int> = MutableLiveData(0)

    val isPlayingFromPlaylist: MutableLiveData<Boolean> = MutableLiveData(false)

    private val sortingType: MutableLiveData<SortingType> = MutableLiveData(SortingType.DATE)
    private val lastCreatedPlaylist: MutableLiveData<Playlist> = MutableLiveData()

    private lateinit var defaultSongList: MutableList<Song>

    fun createPlaylist(states: MutableList<Boolean>) {
        val tmpSongs: MutableList<Song> = mutableListOf()
        states.forEachIndexed { index, state -> if (state) tmpSongs.add(allSongs.value!![index]) }
        lastCreatedPlaylist.value = Playlist(playlistName.value!!, tmpSongs)
    }

    fun createChosenSongList(playlist: Playlist) {
        lastCreatedPlaylist.value = playlist
        val chosenSongListFromUi: MutableList<Boolean> =
            MutableList(allSongs.value!!.size) { false }
        val songsOfPlaylist = playlist.songs
        songsOfPlaylist.forEach {
            val indexOf = allSongs.value!!.indexOf(it)
            chosenSongListFromUi.add(indexOf, true)
        }
        isChosenSongListFromUi.value = chosenSongListFromUi
    }

    fun clearChosenSongList() {
        if (isChosenSongListFromUi.value?.isNotEmpty() == true) isChosenSongListFromUi.value?.clear()
        isChosenSongListFromUi.value = MutableList(allSongs.value!!.size) { false }
    }

    fun changeStateMode() {
        playbackMode.value = when (playbackMode.value as PlaybackMode) {
            PlaybackMode.LOOP -> PlaybackMode.RANDOM
            PlaybackMode.RANDOM -> PlaybackMode.REPEAT
            PlaybackMode.REPEAT -> PlaybackMode.LOOP
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun readSongs() {
        defaultSongList = databaseHelper.readSongs()
        sortSongs(sorting = sortingType.value!!)
    }

    fun savePlaylist() {
        val chosenPlaylist = lastCreatedPlaylist.value
        val playlists = allPlaylists.value!!
        if (playlists.keys.contains(chosenPlaylist)) playlists.remove(chosenPlaylist)
        chosenPlaylist?.let { allPlaylists.value?.put(it, chosenPlaylist.songs) }
        databaseHelper.savePlaylist(chosenPlaylist)
    }

    fun loadPlaylist() {
        allPlaylists.value = databaseHelper.loadPlaylist()
    }

    fun deletePlaylist(currentPlaylist: Playlist) {
        allPlaylists.value = databaseHelper.deletePlaylist(currentPlaylist)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun changeSortingType(sorting: SortingType) {
        sortSongs(sorting)
    }

    private fun sortSongs(sorting: SortingType) {
        allSongs.value =
            when (sorting) {
                SortingType.DATE -> Sorting.byDate(defaultSongList)
                SortingType.RATING -> Sorting.byRating(defaultSongList)
                SortingType.REPEATABILITY -> Sorting.byRepeatability(defaultSongList)
            }
        sortingType.value = sorting
    }

}