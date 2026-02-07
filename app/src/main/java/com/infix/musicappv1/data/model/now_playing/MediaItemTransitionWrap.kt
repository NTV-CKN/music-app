package com.infix.musicappv1.data.model.now_playing

import androidx.media3.common.MediaItem

data class MediaItemTransitionWrap(
    val mediaItem: MediaItem?,
    val index: Int?
)