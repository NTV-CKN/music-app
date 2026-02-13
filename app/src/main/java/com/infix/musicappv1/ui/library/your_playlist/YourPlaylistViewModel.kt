package com.infix.musicappv1.ui.library.your_playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.infix.musicappv1.data.model.playlist.PlaylistWithSongs
import com.infix.musicappv1.data.repository.playlist.PlaylistRepository

class YourPlaylistViewModel(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {
    val playlistCustoms: LiveData<List<PlaylistWithSongs>?> =
        playlistRepository.getLimitPlaylistCustomWithSong().asLiveData()

    class Factory(private val playlistRepository: PlaylistRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(YourPlaylistViewModel::class.java))
                return YourPlaylistViewModel(playlistRepository) as T
            throw IllegalArgumentException("Model class is not suit")
        }
    }
}