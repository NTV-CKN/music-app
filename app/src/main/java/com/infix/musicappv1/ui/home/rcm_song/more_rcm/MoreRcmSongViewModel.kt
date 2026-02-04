package com.infix.musicappv1.ui.home.rcm_song.more_rcm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infix.musicappv1.data.model.song.Song

class MoreRcmSongViewModel : ViewModel() {
    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> = _songs

    fun setSongs(songs: List<Song>) {
        _songs.postValue(songs)
    }
}