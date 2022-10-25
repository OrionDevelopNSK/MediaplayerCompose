package com.orion.mediaplayercompose.data.repositories

import com.orion.mediaplayercompose.data.dao.SongDao
import com.orion.mediaplayercompose.data.entities.SongEntity
import com.orion.mediaplayercompose.data.models.Song

class RoomSongRepository(private val songDao: SongDao) {

    fun insertAllSongs(songs: List<Song>) {
        var songsEntities = mutableListOf<SongEntity>()

        for (song in songs) {
            val songEntity = SongEntity(
                data = song.data,
                title = song.title,
                artist = song.artist,
                duration = song.duration,
                rating = song.rating,
                countOfLaunches = song.countOfLaunches
            )
            songsEntities.add(songEntity)
        }
        songDao.insertAllSongs(songsEntities)
    }

    fun getAll(): List<Song> {
        val songEntities = songDao.getAll()
        val songs = mutableListOf<Song>()

        for (songEntity in songEntities){
            val song = songEntity.toSong()
            songs.add(song)
        }
        return songs
    }
}