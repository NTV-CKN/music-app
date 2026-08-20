package com.infix.musicappv1.data.source.remote.ai_rcm

import com.infix.musicappv1.data.model.ai_rcm.AiRecommendationResponse
import com.infix.musicappv1.data.source.AIRecommendDataSource
import com.infix.musicappv1.data.source.remote.MusicService
import retrofit2.Response
import javax.inject.Inject

class AIRecommendRemoteDataSource @Inject constructor(
    private val musicService: MusicService
) : AIRecommendDataSource.Remote {
    override suspend fun getSongRecommend(body: Map<String, String>): Response<AiRecommendationResponse> {
        return musicService.getSongRecommend(body)
    }
}