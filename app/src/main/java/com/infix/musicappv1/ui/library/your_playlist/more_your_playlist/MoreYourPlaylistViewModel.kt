package com.infix.musicappv1.ui.library.your_playlist.more_your_playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.infix.musicappv1.data.model.playlist.PlaylistWithSongs
import com.infix.musicappv1.data.repository.playlist.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//limit offset
@HiltViewModel
class MoreYourPlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {
    val playlists: LiveData<List<PlaylistWithSongs>?> =
        playlistRepository.getPlaylistCustomWithSong().asLiveData()

//    class Factory(private val playlistRepository: PlaylistRepository) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(MoreYourPlaylistViewModel::class.java))
//                return MoreYourPlaylistViewModel(playlistRepository) as T
//            throw IllegalArgumentException("Model class is not suit!")
//        }
//    }
}