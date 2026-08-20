package com.infix.musicappv1.data.source.local.ai_rcm

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.infix.musicappv1.data.model.ai_rcm.AIRecommendSongEntity

@Dao
interface AIRecommendDAO {
    @Query("DELETE FROM ai_rcm_songs")
    suspend fun clear()

    @Query("""
        SELECT *
        FROM ai_rcm_songs
        ORDER BY id DESC
        LIMIT 1
    """)
    suspend fun getAiRcmSongsLastest(): AIRecommendSongEntity?

    @Insert
    suspend fun insert(aiRcmSongsEntity: AIRecommendSongEntity)
}