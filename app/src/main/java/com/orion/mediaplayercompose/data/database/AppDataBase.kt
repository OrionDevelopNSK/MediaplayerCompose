package com.orion.mediaplayercompose.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.orion.mediaplayercompose.data.dao.PlaylistDao
import com.orion.mediaplayercompose.data.dao.SongDao
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

    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao

    companion object {


        fun getDatabase(context: Context): AppDataBase {
            return Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                "musicDatabase"
            ).build()
        }


    }
}