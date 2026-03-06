package com.infix.musicappv1.ui.playing.now_playing

import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.now_playing.PlayingSong
import com.infix.musicappv1.ui.playing.now_playing.ProcessEventNowPlaying.OnHandleUI
import javax.inject.Inject

//this class will process logic and recall to update UI
class ProcessEventNowPlayingImpl @Inject constructor() : ProcessEventNowPlaying {
    override fun handlePlayPause(
        isPlay: Boolean?,
        mediaController: MediaController?,
        onHandleUI: ProcessEventNowPlaying.OnHandleUI
    ) {
        val isPlaying = isPlay ?: return
        val controller = mediaController ?: return
        var icPauseNext: Int
        if (isPlaying) {
            icPauseNext = R.drawable.ic_play_circle_48px
            controller.pause()
        } else {
            icPauseNext = R.drawable.ic_pause_circle_48px
            controller.prepare()
            controller.play()
        }

        onHandleUI.onHandle(true, icPauseNext)
    }

    override fun handleSkipNext(mediaController: MediaController?, onHandleUI: OnHandleUI) {
        mediaController?.let { controller ->
            if (controller.hasNextMediaItem())
                controller.seekToNext()
        }
    }

    override fun handleSkipPrevious(mediaController: MediaController?, onHandleUI: OnHandleUI) {
        mediaController?.let { controller ->
            if (controller.hasPreviousMediaItem())
                controller.seekToPrevious()
        }
    }

    override fun handleToggleShuffle(mediaController: MediaController?, onHandleUI: OnHandleUI) {
        val isEnable = mediaController?.shuffleModeEnabled ?: return
        mediaController.shuffleModeEnabled = !isEnable

        onHandleUI.onHandle(true, null)
    }

    override fun handleToggleRepeat(mediaController: MediaController?, onHandleUI: OnHandleUI) {
        mediaController?.let { controller ->
            val nowMode = controller.repeatMode
            controller.repeatMode = when (nowMode) {
                Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE
                Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL
                Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_OFF
                else -> Player.REPEAT_MODE_OFF
            }
            onHandleUI.onHandle(true, null)
        }
    }

    override fun handleAddFavorite(
        nowPlayingViewModel: NowPlayingViewModel,
        onHandleUI: OnHandleUI
    ) {
        val isFavorite = nowPlayingViewModel.isFavorite.value ?: return
        val songCurrent = nowPlayingViewModel.playingSongLivedata.value?.song ?: return
        songCurrent.favorite = !isFavorite
        val map = mapOf<String, Any>(
            "id" to songCurrent.id,
            "isFavorite" to songCurrent.favorite
        )

        onHandleUI.onHandle(true, map)
    }

    override fun handleShowOptions(playingSong: PlayingSong?, onHandleUI: OnHandleUI) {
        val song = playingSong?.song ?: return

        onHandleUI.onHandle(true, song)
    }
}