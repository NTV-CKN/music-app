package com.infix.musicappv1.data.source.local.search.song

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infix.musicappv1.data.model.search.SearchKeySong
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchKeySongDao {
    @Query("""
        SELECT * 
        FROM search_key_songs
        ORDER BY search_at DESC
        LIMIT :limit
    """)
    fun getSearchKeySong(limit: Int = 30): Flow<List<SearchKeySong>>

    @Query("""
        DELETE 
        FROM search_key_songs
        WHERE id NOT IN (
            SELECT id 
            FROM search_key_songs
            ORDER BY search_at DESC
            LIMIT :keepLimit
        )
    """)
    suspend fun trimSearchKeySong(keepLimit: Int = 30)

    @Query("DELETE FROM search_key_songs")
    suspend  fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg searchKeySong: SearchKeySong)
}