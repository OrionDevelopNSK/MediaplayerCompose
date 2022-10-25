package com.orion.mediaplayercompose.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orion.mediaplayercompose.data.models.Playlist
import com.orion.mediaplayercompose.data.models.Song

class PlayerViewModel : ViewModel() {

    val playlists : MutableLiveData<Map<Playlist, MutableList<Song>>> = MutableLiveData()
    val songs : MutableLiveData<List<Song>> = MutableLiveData()
    val currentPlaylist : MutableLiveData<Playlist> = MutableLiveData()


    fun savePlaylist(states: MutableList<Boolean>){
        states.forEach { println(it) }
        println("Click")
    }
}