package com.infix.musicappv1.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.session.MediaController

class PlaybackViewModel : ViewModel() {
    private val _mediaController = MutableLiveData<MediaController>()
    val mediaController: LiveData<MediaController> = _mediaController

    fun setMediaController(mediaController: MediaController) {
        _mediaController.postValue(mediaController)
    }
}