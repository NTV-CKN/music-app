package com.infix.musicappv1.ui.playing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.media3.common.MediaItem
import com.infix.musicappv1.data.repository.PlaybackRepository
import kotlinx.coroutines.flow.MutableStateFlow

class MiniPlayerViewModel(
    private val playbackRepository: PlaybackRepository
) : ViewModel() {
//    private val _mediaItems = MutableStateFlow<List<MediaItem>?>(null)
//    val mediaItems: StaF<List<MediaItem>?> = _mediaItems
    val isFavorite: LiveData<Boolean> = playbackRepository.isFavorite.asLiveData().distinctUntilChanged()
    val isPlaying: LiveData<Boolean?> = playbackRepository.isPlaying.asLiveData()

//    fun setSong(song: Song?) {
//        _song.postValue(song)
//        song?.let {
//            Log.d("SVU", song.toString())
//            val mediaItem = MediaItem.Builder().setUri(song.source)
//            setMediaItem(mediaItem.build())
//        }
//    }

//    fun setMediaItems(mediaItems: List<MediaItem>?) {
//        _mediaItems.value = mediaItems
//    }

//
//    fun setPlaying(bool: Boolean) {
//        _isPlaying.value = bool
//    }
}