package com.infix.musicappv1.ui.home.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.repository.album.AlbumRepository
import com.infix.musicappv1.data.source.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlbumHotViewModel(private val albumRepository: AlbumRepository) : ViewModel() {
    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> = _albums

    init {
        loadAlbums()
    }

    private fun loadAlbums() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = albumRepository.loadAlbums()
            if (result is Result.Success) {
                _albums.postValue(result.data.albums)
            } else {
                _albums.postValue(emptyList())
            }
        }
    }
}