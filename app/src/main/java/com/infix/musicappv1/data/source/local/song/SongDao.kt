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
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg song: Song)

    @Delete
    suspend fun delete(vararg song: Song)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(vararg song: Song)

    @Query(
        """
        SELECT *
        FROM songs
    """
    )
    fun getAllSongsFlow(): Flow<List<Song>>

    @Query(
        """
        SELECT *
        FROM songs
    """
    )
    suspend fun getAllSongs(): List<Song>

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
        SELECT *
        FROM songs 
        WHERE favorite = 1
        LIMIT :limit
    """
    )
    fun getSongsFavoriteWithLimit(limit: Int = 10): Flow<List<Song>>

    @Query(
        """
            UPDATE songs
            SET favorite = :isFavorite
            WHERE song_id = :id
        """
    )
    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    @Query(
        """
        SELECT *
        FROM  songs
        ORDER BY counter DESC
        LIMIT 15
    """
    )
    fun getTop15SongMostHeard(): Flow<List<Song>>

    @Query(
        """
        SELECT *
        FROM  songs
        ORDER BY counter DESC
        LIMIT 40
    """
    )
    fun getTop40SongMostHeard(): Flow<List<Song>>
}