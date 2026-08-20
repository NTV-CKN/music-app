package com.infix.musicappv1.data.source

import com.infix.musicappv1.data.model.ai_rcm.AIRecommendSongEntity
import com.infix.musicappv1.data.model.ai_rcm.AiRecommendationResponse
import retrofit2.Response

interface AIRecommendDataSource {
    interface Remote {
        suspend fun getSongRecommend(body: Map<String, String>): Response<AiRecommendationResponse>
    }

    interface Local {
        suspend fun clear()
        suspend fun getSongRecommendLastest(): AIRecommendSongEntity?
        suspend fun insert(aiRcmSongsEntity: AIRecommendSongEntity)
    }
}