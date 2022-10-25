package com.orion.mediaplayercompose.utils

import com.orion.mediaplayercompose.data.models.Song

class Sorting {

    companion object {
        fun byDate(songs: List<Song>): List<Song> {
            return songs.asReversed()
        }

        fun byRating(songs: List<Song>): List<Song> {
            return songs.sortedBy { it.rating }.asReversed()
        }

        fun byRepeatability(songs: List<Song>): List<Song> {
            return songs.sortedBy { it.countOfLaunches }.asReversed()
        }
    }
}