package com.infix.musicappv1.ui.home.album.more_album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infix.musicappv1.data.model.album.Album

class MoreAlbumViewModel : ViewModel() {
    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> = _albums

    fun setAlbums(albums: List<Album>) {
        _albums.postValue(albums)
    }
}