package com.infix.musicappv1.ui.home.system_playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.repository.playlist.PlaylistRepository
import com.infix.musicappv1.data.source.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SystemPlaylistViewModel(private val playlistRepository: PlaylistRepository) : ViewModel() {
    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists

    fun setPlaylists(playlists: List<Playlist>) {
        _playlists.postValue(playlists)
    }

    class Factory(private val playlistRepository: PlaylistRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SystemPlaylistViewModel::class.java))
                return SystemPlaylistViewModel(playlistRepository) as T
            throw IllegalArgumentException("Model class is not suit")
        }
    }
}