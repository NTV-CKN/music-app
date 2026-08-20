package com.infix.musicappv1.data.repository.ai_rcm

import com.infix.musicappv1.data.model.ai_rcm.AiRecommendationResponse
import com.infix.musicappv1.data.source.AIRecommendDataSource
import com.infix.musicappv1.data.source.Result;
import javax.inject.Inject

class AIRecommendRepositoryImpl @Inject constructor(
    private val remote: AIRecommendDataSource.Remote
) : IAIRecommendRepository {
    override suspend fun loadSongRecommend(promptClient: String): Result<AiRecommendationResponse> {
        try {
            val body = mapOf(
                "promptClient" to promptClient
            )

            val response = remote.getSongRecommend(body)
            if (response.isSuccessful) {
                return Result.Success(
                    response.body() ?: throw Exception("Dữ liệu trả về null")
                )
            }

            throw Exception("Không thể tải được bài hát từ AI")
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }
}