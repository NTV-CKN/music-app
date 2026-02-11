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

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg playlist: Playlist)

    @Delete
    suspend fun delete(vararg playlist: Playlist)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(vararg playlist: Playlist)

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
}