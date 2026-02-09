package com.infix.musicappv1.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.infix.musicappv1.data.repository.PlaybackRepository
import com.infix.musicappv1.ui.playing.MiniPlayerViewModel

class Factory(private val playbackRepository: PlaybackRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayingSongSharedViewModel::class.java))
            return PlayingSongSharedViewModel(playbackRepository) as T
        else if (modelClass.isAssignableFrom(MiniPlayerViewModel::class.java))
            return MiniPlayerViewModel(playbackRepository) as T
        throw IllegalArgumentException("Model class is not legal")
    }
}