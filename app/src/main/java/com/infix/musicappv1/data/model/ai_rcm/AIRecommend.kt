package com.infix.musicappv1.data.model.ai_rcm

import com.infix.musicappv1.data.model.song.Song

data class AiRecommendationResponse(
    val aiMessage: String = "",
    val promptSummary: String = "",
    val songs: List<Song> = emptyList()
)

sealed class AiMoodUiState {
    object Idle : AiMoodUiState()
    object Loading : AiMoodUiState()
    data class Success(val response: AiRecommendationResponse) : AiMoodUiState()
    data class Error(val message: String) : AiMoodUiState()
}