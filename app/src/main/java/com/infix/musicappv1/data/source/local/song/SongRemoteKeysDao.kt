package com.infix.musicappv1.data.source.local.song

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infix.musicappv1.data.model.song.SongRemoteKeys

@Dao
interface SongRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg songRemoteKeys: SongRemoteKeys)

    @Query("""
        SELECT *
        FROM song_remote_keys
        WHERE song_id = :songId
    """)
    suspend fun getSongRemoteKeysById(songId: String): SongRemoteKeys?

    @Query("""
        SELECT *
        FROM song_remote_keys
        ORDER BY create_at DESC
        LIMIT 1
    """)
    suspend fun getSongRemoteKeyLastest(): SongRemoteKeys?

    @Query(
        """
        DELETE
        FROM song_remote_keys
    """
    )
    suspend fun clear()
}