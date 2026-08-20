package com.infix.musicappv1.data.source.local.ai_rcm

import com.infix.musicappv1.data.model.ai_rcm.AIRecommendSongEntity
import com.infix.musicappv1.data.source.AIRecommendDataSource
import javax.inject.Inject

class AIRecommendLocalDataSource @Inject constructor(
    private val aiRcmDAO: AIRecommendDAO
) : AIRecommendDataSource.Local {
    override suspend fun clear() {
        return aiRcmDAO.clear()
    }

    override suspend fun getSongRecommendLastest(): AIRecommendSongEntity? {
        return aiRcmDAO.getAiRcmSongsLastest()
    }

    override suspend fun insert(aiRcmSongsEntity: AIRecommendSongEntity) {
        return aiRcmDAO.insert(aiRcmSongsEntity)
    }
}