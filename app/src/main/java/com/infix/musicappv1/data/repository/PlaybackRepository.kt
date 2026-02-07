package com.infix.musicappv1.data.repository

import com.infix.musicappv1.data.model.now_playing.MediaItemTransitionWrap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlaybackRepository private constructor() {
    private val _mediaItemTransition: MutableStateFlow<MediaItemTransitionWrap?> =
        MutableStateFlow(null)
    val mediaItemTransition: StateFlow<MediaItemTransitionWrap?> = _mediaItemTransition

    fun updateMediaTransition(mediaItemTransitionWrap: MediaItemTransitionWrap?) {
        _mediaItemTransition.value = mediaItemTransitionWrap
    }

    companion object {
        val instance = PlaybackRepository()
    }
}