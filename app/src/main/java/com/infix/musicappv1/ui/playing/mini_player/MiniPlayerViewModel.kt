package com.infix.musicappv1.ui.playing.mini_player

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import com.infix.musicappv1.data.repository.PlaybackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MiniPlayerViewModel @Inject constructor(
    private val playbackRepository: PlaybackRepository
) : ViewModel() {
    val isFavorite: LiveData<Boolean> = playbackRepository.isFavorite.asLiveData().distinctUntilChanged()
    val isPlaying: LiveData<Boolean?> = playbackRepository.isPlaying.asLiveData()
}