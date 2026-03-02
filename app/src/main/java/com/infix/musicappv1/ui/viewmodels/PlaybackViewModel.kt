package com.infix.musicappv1.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.media3.session.MediaController
import com.infix.musicappv1.media.MediaControllerProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlaybackViewModel @Inject constructor(): ViewModel() {
    //    private val _mediaController = MutableLiveData<MediaController?>()
    val mediaController: LiveData<MediaController?> =
        MediaControllerProvider.controllerFlow.asLiveData()
}