package com.infix.musicappv1.ui.home.system_playlist.more_album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infix.musicappv1.data.model.playlist.Playlist

class MoreAlbumViewModel : ViewModel() {
    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists

    fun setPlaylists(playlists: List<Playlist>) {
        _playlists.postValue(playlists)
    }
}