package com.infix.musicappv1.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.repository.playlist.PlaylistRepository

class PlaylistDetailViewModel(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {
    //limit offset when open playlist detail fragment and update for _playlist
    private val _playlist = MutableLiveData<Playlist?>()
    val playlist: LiveData<Playlist?> = _playlist

    fun setPlaylist(playlist: Playlist) {
        _playlist.value = playlist
    }

    suspend fun getPlaylistWithName(name: String): Playlist? {
        return playlistRepository.getPlaylistWithName(name)
    }

    class Factory(private val playlistRepository: PlaylistRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlaylistDetailViewModel::class.java))
                return PlaylistDetailViewModel(playlistRepository) as T

            throw IllegalArgumentException("Model class is not legal")
        }
    }
}