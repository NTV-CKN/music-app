package com.infix.musicappv1.data.source.local.recent

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infix.musicappv1.data.model.recent.SongRecent
import kotlinx.coroutines.flow.Flow

@Dao
interface SongRecentDao {
    @Query(
        """
        SELECT *
        FROM song_recents
        ORDER BY play_at DESC
        LIMIT :limit
    """
    )
    fun getSongRecents(limit: Int = 30): Flow<List<SongRecent>>

    @Query(
        """
        DELETE FROM song_recents
        WHERE song_id NOT IN(
            SELECT song_id
            FROM song_recents
            ORDER BY play_at DESC
            LIMIT :keepLimit
        )
    """
    )
    suspend fun trimSongRecents(keepLimit: Int = 30)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(songRecent: SongRecent): Long
}