package com.infix.musicappv1.data.repository.ai_rcm

import android.util.Log
import com.infix.musicappv1.data.model.ai_rcm.AIRecommendSongEntity
import com.infix.musicappv1.data.model.ai_rcm.AiRecommendationResponse
import com.infix.musicappv1.data.source.AIRecommendDataSource
import com.infix.musicappv1.data.source.Result;
import javax.inject.Inject

class AIRecommendRepositoryImpl @Inject constructor(
    private val remote: AIRecommendDataSource.Remote,
    private val local: AIRecommendDataSource.Local
) : IAIRecommendRepository {
    override suspend fun loadSongRecommend(promptClient: String): Result<AiRecommendationResponse> {
        try {
            val body = mapOf(
                "promptClient" to promptClient
            )

            val response = remote.getSongRecommend(body)
            if (response.isSuccessful) {
                val body = response.body() ?: throw Exception("Dữ liệu trả về null")
                updateRecommendSongs(body)
                return Result.Success(
                    response.body()!!
                )
            }

            throw Exception("Không thể tải được bài hát từ AI")
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    override suspend fun songRecommendFromDb(): Result<AiRecommendationResponse> {
        try {
            val entity: AIRecommendSongEntity = (local.getSongRecommendLastest()
                ?: Exception("Song recommend is null")) as AIRecommendSongEntity

            val aiRcmResponse = AiRecommendationResponse(
                aiMessage = entity.aiMessage,
                promptSummary = entity.promptSummary,
                songs = entity.songsJsonToList()
            )

            return Result.Success(aiRcmResponse)
        } catch (ex: Exception) {
            Log.e("AIRecommendRepositoryImpl", ex.message?:"s")
            return Result.Error(ex)
        }
    }

    override suspend fun updateRecommendSongs(aiRecommendationResponse: AiRecommendationResponse) {
        local.clear()
        val aiRecommendSongEntity = AIRecommendSongEntity(
            aiMessage = aiRecommendationResponse.aiMessage,
            promptSummary = aiRecommendationResponse.promptSummary
        )
        aiRecommendSongEntity.songsListToJson(aiRecommendationResponse.songs)

        local.insert(aiRecommendSongEntity)
    }
}