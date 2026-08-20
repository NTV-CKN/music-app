package com.infix.musicappv1.data.repository.ai_rcm

import com.infix.musicappv1.data.model.ai_rcm.AiRecommendationResponse
import com.infix.musicappv1.data.source.Result

interface IAIRecommendRepository {
    suspend fun loadSongRecommend(promptClient: String): Result<AiRecommendationResponse>
}