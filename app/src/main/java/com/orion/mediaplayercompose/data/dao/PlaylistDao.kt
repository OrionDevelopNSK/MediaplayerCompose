package com.orion.mediaplayercompose.data.dao

import androidx.room.*
import com.orion.mediaplayercompose.data.entities.PlaylistEntity
import com.orion.mediaplayercompose.data.entities.PlaylistSongEntity
import com.orion.mediaplayercompose.data.entities.SongEntity
import com.orion.mediaplayercompose.data.models.Playlist
import com.orion.mediaplayercompose.data.models.Song

@Dao
abstract class PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPlaylist(playlists: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPlaylists(playlistSongEntityList: List<PlaylistSongEntity>)

    @Query("SELECT * FROM playlists")
    abstract fun getAll(): List<PlaylistEntity>

    @Transaction
    @Query("SELECT * FROM playlist_song WHERE playlistName =:name")
    abstract fun getPlaylistSongEntities(name: String): List<PlaylistSongEntity>

    @Query("SELECT * FROM songs WHERE data =:data")
    abstract fun getSongEntity(data: String): SongEntity

    @Query("DELETE FROM playlist_song WHERE playlistName = :name")
    abstract fun deleteByPlaylistNameFromLinkingTable(name: String)

    @Query("DELETE FROM playlists WHERE playlistName = :name")
    abstract fun deleteByPlaylistNameFromPlaylistTable(name: String)

    @Transaction
    open fun deletePlaylist(playlist: PlaylistEntity) {
        deleteByPlaylistNameFromLinkingTable(playlist.playlistName)
        deleteByPlaylistNameFromPlaylistTable(playlist.playlistName)
    }

    @Transaction
    open fun insertPlaylistAndSoundTrack(
        playlistEntity: PlaylistEntity,
        playlistSongEntityList: List<PlaylistSongEntity>
    ) {
        insertPlaylist(playlistEntity)
        insertPlaylists(playlistSongEntityList)
    }


    open fun getPlaylistWithSoundTrack(): Map<Playlist, MutableList<Song>> {
        val tmp = HashMap<Playlist, MutableList<Song>>()
        val all = getAll()

        for (playlistEntity in all) {
            val playlistsWithSongs = getPlaylistSongEntities(playlistEntity.playlistName)
            val listSongEntity: MutableList<SongEntity> = mutableListOf()
            val tmpSong: MutableList<Song> = mutableListOf()

            for (p in playlistsWithSongs) {
                val songEntity = getSongEntity(p.data)
                listSongEntity.add(songEntity)
                tmpSong.add(songEntity.toSong())
            }

            playlistEntity.songEntities = listSongEntity
            tmp[playlistEntity.toPlaylist()] = tmpSong
        }
        return tmp
    }

}