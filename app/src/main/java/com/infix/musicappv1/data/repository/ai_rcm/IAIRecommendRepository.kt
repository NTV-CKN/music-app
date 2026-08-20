package com.infix.musicappv1.data.repository.ai_rcm

import com.infix.musicappv1.data.model.ai_rcm.AiRecommendationResponse
import com.infix.musicappv1.data.source.Result

interface IAIRecommendRepository {
    //remote
    suspend fun loadSongRecommend(promptClient: String): Result<AiRecommendationResponse>

    //local
    suspend fun songRecommendFromDb(): Result<AiRecommendationResponse>
    suspend fun updateRecommendSongs(aiRecommendationResponse: AiRecommendationResponse)
}