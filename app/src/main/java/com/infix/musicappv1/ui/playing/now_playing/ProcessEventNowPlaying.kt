package com.infix.musicappv1.ui.playing.now_playing

import androidx.media3.session.MediaController
import com.infix.musicappv1.data.model.now_playing.PlayingSong

interface ProcessEventNowPlaying {
    fun handlePlayPause(isPlay: Boolean?, mediaController: MediaController?, onHandleUI: OnHandleUI)
    fun handleSkipNext(mediaController: MediaController?, onHandleUI: OnHandleUI)
    fun handleSkipPrevious(mediaController: MediaController?, onHandleUI: OnHandleUI)
    fun handleToggleShuffle(mediaController: MediaController?, onHandleUI: OnHandleUI)
    fun handleToggleRepeat(mediaController: MediaController?, onHandleUI: OnHandleUI)
    fun handleAddFavorite(nowPlayingViewModel: NowPlayingViewModel, onHandleUI: OnHandleUI)
    fun handleShowOptions(playingSong: PlayingSong?, onHandleUI: OnHandleUI)

    fun interface OnHandleUI {
        fun onHandle(isSuccess: Boolean, data: Any?)
    }
}