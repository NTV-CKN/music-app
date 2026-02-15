package com.infix.musicappv1.data.source.local.playlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.playlist.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Query(
        """
        SELECT *
        FROM playlists
        WHERE is_custom = 1
        LIMIT :limit
    """
    )
    fun getPlaylistCustomWithLimit(limit: Int = 10): Flow<List<Playlist>>

    @Query(
        """
        SELECT *
        FROM playlists
        WHERE is_custom = 1
    """
    )
    fun getAllPlaylistCustom(): Flow<List<Playlist>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg playlist: Playlist)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertStrict(playlist: Playlist)

    @Delete
    suspend fun delete(vararg playlist: Playlist)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(vararg playlist: Playlist)

    @Query(
        """
        SELECT *
        FROM playlists
        WHERE name = :name
    """
    )
    suspend fun getPlaylistWithName(name: String): Playlist?

    @Query(
        """
        SELECT *
        FROM playlists
        WHERE playlist_id = :id
    """
    )
    suspend fun getPlaylistById(id: Int): Playlist?

    @Transaction
    @Query("SELECT * FROM playlists WHERE playlist_id = :playlistId")
    suspend fun getPlaylistWithSongsById(playlistId: Int): PlaylistWithSongs?

    @Transaction
    @Query(
        """
        SELECT * 
        FROM playlists
        WHERE is_custom = 1
    """
    )
    fun getPlaylistCustomWithSong(): Flow<List<PlaylistWithSongs>?>

    @Transaction
    @Query(
        """
        SELECT * 
        FROM playlists
        WHERE is_custom = 1
        LIMIT :limit
    """
    )
    fun getLimitPlaylistCustomWithSong(limit: Int = 10): Flow<List<PlaylistWithSongs>?>
}