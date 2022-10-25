package com.orion.mediaplayercompose.data.dao

import androidx.room.*
import com.orion.mediaplayercompose.data.entities.SongEntity

@Dao
interface SongDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllSongs(list: MutableList<SongEntity>)

    @Query("SELECT * FROM songs")
    fun getAll(): MutableList<SongEntity>

}