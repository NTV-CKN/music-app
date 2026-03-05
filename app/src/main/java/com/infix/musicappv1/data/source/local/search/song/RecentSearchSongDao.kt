package com.infix.musicappv1.data.source.local.search.song

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.infix.musicappv1.data.model.search.RecentSearchSong
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchSongDao {
    @Query("""
        SELECT * 
        FROM recent_search_songs
        ORDER BY play_at DESC
        LIMIT :limit
    """)
    fun getRecentSearchSong(limit: Int = 30): Flow<List<RecentSearchSong>>

    @Query("""
        DELETE 
        FROM recent_search_songs
        WHERE song_id NOT IN (
            SELECT song_id 
            FROM recent_search_songs
            ORDER BY play_at DESC
            LIMIT :keepLimit
        )
    """)
    fun trimRecentSearchSong(keepLimit: Int = 30)

    @Delete
    fun clear()
}