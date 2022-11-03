package com.orion.mediaplayercompose.data.dao

import androidx.room.*
import com.orion.mediaplayercompose.data.entities.PlaylistEntity
import com.orion.mediaplayercompose.data.entities.PlaylistSongEntity
import com.orion.mediaplayercompose.data.entities.SongEntity
import com.orion.mediaplayercompose.data.models.Playlist
import com.orion.mediaplayercompose.data.models.Song

@Dao
abstract class RoomDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAllSongs(list: MutableList<SongEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertPlaylist(playlists: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertPlaylists(playlistSongEntityList: List<PlaylistSongEntity>)

    @Query("SELECT * FROM playlists")
    abstract suspend fun getAll(): List<PlaylistEntity>

    @Transaction
    @Query("SELECT * FROM playlist_song WHERE playlistName =:name")
    abstract suspend fun getPlaylistSongEntities(name: String): List<PlaylistSongEntity>

    @Query("SELECT * FROM songs WHERE data =:data")
    abstract suspend fun getSongEntity(data: String): SongEntity

    @Query("DELETE FROM playlist_song WHERE playlistName = :name")
    abstract suspend fun deleteByPlaylistNameFromLinkingTable(name: String)

    @Query("DELETE FROM playlists WHERE playlistName = :name")
    abstract suspend fun deleteByPlaylistNameFromPlaylistTable(name: String)

    @Transaction
    open suspend fun deletePlaylist(playlist: PlaylistEntity) {
        deleteByPlaylistNameFromLinkingTable(playlist.playlistName)
        deleteByPlaylistNameFromPlaylistTable(playlist.playlistName)
    }

    @Transaction
    open suspend fun insertPlaylistAndSoundTrack(
        playlistEntity: PlaylistEntity,
        playlistSongEntityList: List<PlaylistSongEntity>
    ) {
        insertPlaylist(playlistEntity)
        insertPlaylists(playlistSongEntityList)
    }

    suspend fun getPlaylistWithSoundTrack(): MutableMap<Playlist, MutableList<Song>> {
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