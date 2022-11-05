package com.orion.mediaplayercompose.models

import android.media.MediaPlayer
import com.orion.mediaplayercompose.data.models.Song
import com.orion.mediaplayercompose.utils.PlaybackMode.*
import com.orion.mediaplayercompose.viewmodels.PlayerViewModel

class Player {

    private val mediaPlayer = MediaPlayer()
    private lateinit var viewModel: PlayerViewModel
    private lateinit var currentPlayingSong: Song

    fun setPlayerViewModel(viewModel: PlayerViewModel) {
        this.viewModel = viewModel
    }

    fun start(song: Song) {
        currentPlayingSong = song
        mediaPlayer.reset()
        mediaPlayer.setDataSource(song.data)
        mediaPlayer.setOnPreparedListener { mediaPlayer: MediaPlayer ->
            mediaPlayer.start()
        }
        mediaPlayer.prepareAsync()
    }

    fun completeSongPlaying() {
        mediaPlayer.setOnCompletionListener {
            next()
        }
    }

    fun switchMode() {
        viewModel.playbackMode.value = when (viewModel.playbackMode.value!!) {
            LOOP -> RANDOM
            RANDOM -> REPEAT
            REPEAT -> LOOP
        }
    }

    fun stop() {
        mediaPlayer.stop()
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun next() {
        val songs = when {
            viewModel.isPlayingFromPlaylist.value!! -> {
                viewModel.allPlaylists.value?.get(viewModel.currentPlaylist.value)!!
            }
            else -> {
                viewModel.allSongs.value!! as MutableList<Song>
            }
        }
        nextFromForCurrentPlaybackMode(songs)
    }

    fun previous() {
        val songs = when {
            viewModel.isPlayingFromPlaylist.value!! -> {
                viewModel.allPlaylists.value?.get(viewModel.currentPlaylist.value)!!
            }
            else -> {
                viewModel.allSongs.value!!
            }
        }
        previousForCurrentPlaybackMode(songs)
    }

    fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    fun setCurrentPosition(position: Long) {
        mediaPlayer.seekTo(position.toInt())
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    fun setVolume(levelVolume: Float) {
        mediaPlayer.setVolume(levelVolume, levelVolume)
    }

    private fun previousForCurrentPlaybackMode(songs: List<Song>) {
        if (viewModel.playbackMode.value == RANDOM) {
            start(songs.random())
        }else{
            when (currentPlayingSong) {
                songs.first() -> start(songs.last())
                else -> {
                    val currentIndexSong = songs.indexOf(currentPlayingSong)
                    start(songs[currentIndexSong - 1])
                }
            }
        }
    }

    private fun nextFromForCurrentPlaybackMode(songs: List<Song>) {
        if (viewModel.playbackMode.value == RANDOM) {
            start(songs.random())
        }else{
            when (currentPlayingSong) {
                songs.last() -> start(songs.first())
                else -> {
                    val currentIndexSong = songs.indexOf(currentPlayingSong)
                    start(songs[currentIndexSong + 1])
                }
            }
        }
    }




}