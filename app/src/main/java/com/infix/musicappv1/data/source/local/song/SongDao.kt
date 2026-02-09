package com.infix.musicappv1.data.source.local.song

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.infix.musicappv1.data.model.song.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg song: Song)

    @Delete
    suspend fun delete(vararg song: Song)

    @Update
    suspend fun update(vararg song: Song)

    @Query("""
        SELECT *
        FROM songs
    """)
    fun getAllSongs(): Flow<List<Song>>

    @Query(
        """
            SELECT *
            FROM songs 
            WHERE favorite = 1
        """
    )
    fun getSongsFavorite(): Flow<List<Song>>

    @Query(
        """
            UPDATE songs
            SET favorite = :isFavorite
            WHERE id = :id
        """
    )
    suspend fun updateFavorite(id: String, isFavorite: Boolean)
}