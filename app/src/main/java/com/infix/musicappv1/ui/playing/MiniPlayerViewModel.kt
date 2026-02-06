package com.infix.musicappv1.ui.playing

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.infix.musicappv1.data.model.song.Song

class MiniPlayerViewModel : ViewModel() {
    private val _song = MutableLiveData<Song?>()
    val song: LiveData<Song?> = _song

    private val _mediaItem = MutableLiveData<MediaItem?>()
    val mediaItem: LiveData<MediaItem?> = _mediaItem

    private val _isPlaying = MutableLiveData<Boolean?>()
    val isPlaying: LiveData<Boolean?> = _isPlaying

    fun setSong(song: Song?) {
        _song.postValue(song)
        song?.let {
            Log.d("SVU", song.toString())
            val mediaItem = MediaItem.Builder().setUri(song.source)
            setMediaItem(mediaItem.build())
        }
    }

    fun setPlaying(bool: Boolean) {
        _isPlaying.value = bool
    }

    private fun setMediaItem(mediaItem: MediaItem) {
        _mediaItem.postValue(mediaItem)
    }
}