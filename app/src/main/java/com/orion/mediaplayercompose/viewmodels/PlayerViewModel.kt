package com.orion.mediaplayercompose.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orion.mediaplayercompose.data.models.Playlist
import com.orion.mediaplayercompose.data.models.Song
import com.orion.mediaplayercompose.models.*
import com.orion.mediaplayercompose.utils.PlaybackMode
import com.orion.mediaplayercompose.utils.SortingType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val player: Player,
    private val songSorter: SongSorter,
    private val songsReader: SongsReader,
    private val playlistController: PlaylistController,
    private val stateModeChanger: StateModeChanger,
    private val chosenSongListController: ChosenSongListController
) : ViewModel() {

    val allPlaylists: MutableLiveData<MutableMap<Playlist, MutableList<Song>>> = MutableLiveData()
    val allSongs: MutableLiveData<List<Song>> = MutableLiveData(mutableListOf())

    val isMediaPlayerPlayed: MutableLiveData<Boolean> = MutableLiveData(false)
    val isPlayingFromPlaylist: MutableLiveData<Boolean> = MutableLiveData(false)

    val currentPlaylist: MutableLiveData<Playlist> = MutableLiveData()
    val currentSong: MutableLiveData<Song> = MutableLiveData()
    val currentTabPosition: MutableLiveData<Int> = MutableLiveData(0)
    val currentPosition: MutableLiveData<String> = MutableLiveData()

    val sortingType: MutableLiveData<SortingType> = MutableLiveData(SortingType.DATE)
    val playbackMode: MutableLiveData<PlaybackMode> = MutableLiveData(PlaybackMode.LOOP)

    val playlistName: MutableLiveData<String> = MutableLiveData("")
    val isChosenSongListFromUi = MutableLiveData<MutableList<Boolean>>(mutableListOf())

    val sliderThumbPosition: MutableLiveData<Float> = MutableLiveData()
    val defaultSongList: MutableLiveData<MutableList<Song>> = MutableLiveData()

    val lastCreatedPlaylist: MutableLiveData<Playlist> = MutableLiveData()

    init {
        player.setPlayerViewModel(this)
        songSorter.setPlayerViewModel(this)
        songsReader.setPlayerViewModel(this)
        playlistController.setPlayerViewModel(this)
        stateModeChanger.setPlayerViewModel(this)
        chosenSongListController.setPlayerViewModel(this)
    }

    fun getMediaPlayer(): Player = player
    fun getSongSorter(): SongSorter = songSorter
    fun getSongsReader(): SongsReader = songsReader
    fun getPlaylistController(): PlaylistController = playlistController
    fun getStateModeChanger(): StateModeChanger = stateModeChanger
    fun getChosenSongListController(): ChosenSongListController = chosenSongListController

}