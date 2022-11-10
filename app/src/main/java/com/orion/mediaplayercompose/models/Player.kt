package com.orion.mediaplayercompose.models

import android.media.MediaPlayer
import com.orion.mediaplayercompose.data.models.Song
import com.orion.mediaplayercompose.utils.PlaybackMode.*
import com.orion.mediaplayercompose.utils.TaskLooper
import com.orion.mediaplayercompose.utils.extentions.toMinutesAndSeconds
import com.orion.mediaplayercompose.utils.toMinutesAndSeconds
import com.orion.mediaplayercompose.viewmodels.PlayerViewModel
import javax.inject.Inject
import javax.inject.Singleton

interface IPlayer {
    fun start(song: Song)
    fun switchMode()
    fun stop()
    fun pause()
    fun next()
    fun previous()
    fun isPlaying(): Boolean
    fun setCurrentPosition(position: Long)
    fun getCurrentPosition(): Int
    fun setVolume(levelVolume: Float)
}

@Singleton
class Player @Inject constructor() : IPlayer {

    private lateinit var viewModel: PlayerViewModel
    private var currentPlayingSong: Song? = null

    private val mediaPlayer = MediaPlayer()
    private val taskLooper: TaskLooper = TaskLooper(
        {
            viewModel.sliderThumbPosition.value =
                viewModel.currentSong.value?.duration?.let { 1f.div(it) }
                    ?.times(getCurrentPosition())

            viewModel.currentPosition.value = getCurrentPosition().toMinutesAndSeconds()
        },
        1000
    )

    fun setPlayerViewModel(viewModel: PlayerViewModel) {
        this.viewModel = viewModel
        completeSongPlaying()
    }

    private fun completeSongPlaying() {
        mediaPlayer.setOnCompletionListener {
            next()
        }
    }

    private fun refreshSongsButtonStates() {
        viewModel.allSongs.value?.forEach {
            if (it.isPlayed) it.isPlayed = false
        }

        viewModel.allPlaylists.value?.get(viewModel.currentPlaylist.value)?.toList()?.forEach {
            if (it.isPlayed) it.isPlayed = false
        }
    }

    override fun start(song: Song) {
        refreshSongsButtonStates()
        if (song == currentPlayingSong && mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            song.isPlayed = false
            viewModel.isMediaPlayerPlayed.value = false
            return
        } else if (song == currentPlayingSong && !mediaPlayer.isPlaying) {
            mediaPlayer.start()
            song.isPlayed = true
            viewModel.isMediaPlayerPlayed.value = true
            return
        }
        song.isPlayed = true
        viewModel.isMediaPlayerPlayed.value = true
        currentPlayingSong = song
        mediaPlayer.reset()
        mediaPlayer.setDataSource(song.data)
        mediaPlayer.setOnPreparedListener { mediaPlayer: MediaPlayer ->
            mediaPlayer.start()
        }
        mediaPlayer.prepareAsync()
        viewModel.currentSong.value = song
        if (song.isPlayed) taskLooper.startTask() else taskLooper.stopTask()
    }

    override fun switchMode() {
        viewModel.playbackMode.value = when (viewModel.playbackMode.value!!) {
            LOOP -> RANDOM
            RANDOM -> REPEAT
            REPEAT -> LOOP
        }
    }

    override fun stop() {
        mediaPlayer.stop()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun next() {
        val songs = when (viewModel.isPlayingFromPlaylist.value) {
            true -> {
                viewModel.allPlaylists.value?.get(viewModel.currentPlaylist.value)
            }
            else -> {
                viewModel.allSongs.value
            }
        }
        songs?.let { nextForCurrentPlaybackMode(it) }
    }

    override fun previous() {
        val songs = when (viewModel.isPlayingFromPlaylist.value) {
            true -> {
                viewModel.allPlaylists.value?.get(viewModel.currentPlaylist.value)
            }
            else -> {
                viewModel.allSongs.value
            }
        }
        songs?.let { previousForCurrentPlaybackMode(it) }
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun setCurrentPosition(position: Long) {
        viewModel.currentPosition.value = position.toMinutesAndSeconds()
        mediaPlayer.seekTo(position.toInt())
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun setVolume(levelVolume: Float) {
        mediaPlayer.setVolume(levelVolume, levelVolume)
    }

    private fun previousForCurrentPlaybackMode(songs: List<Song>) {
        if (viewModel.playbackMode.value == RANDOM) {
            start(songs.random())
        } else {
            when (currentPlayingSong) {
                songs.first() -> start(songs.last())
                else -> {
                    val currentIndexSong = songs.indexOf(currentPlayingSong)
                    start(songs[currentIndexSong - 1])
                }
            }
        }
    }

    private fun nextForCurrentPlaybackMode(songs: List<Song>) {
        if (viewModel.playbackMode.value == RANDOM) {
            start(songs.random())
        } else {
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


