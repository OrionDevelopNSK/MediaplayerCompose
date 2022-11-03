package com.orion.mediaplayercompose.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.orion.mediaplayercompose.data.dao.RoomDao
import com.orion.mediaplayercompose.data.entities.PlaylistEntity
import com.orion.mediaplayercompose.data.entities.PlaylistSongEntity
import com.orion.mediaplayercompose.data.entities.SongEntity

@Database(
    entities = [
        SongEntity::class,
        PlaylistEntity::class,
        PlaylistSongEntity::class
    ],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun roomDao(): RoomDao
}