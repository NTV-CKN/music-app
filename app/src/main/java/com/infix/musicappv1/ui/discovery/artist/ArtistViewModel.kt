package com.infix.musicappv1.ui.discovery.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.repository.artist.ArtistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtistViewModel(
    private val artistRepository: ArtistRepository
) : ViewModel() {

    fun updateInterestedArtist(artist: Artist) {
        viewModelScope.launch(Dispatchers.IO) {
            artistRepository.update(artist)
        }
    }

    class Factory(
        private val artistRepository: ArtistRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistViewModel::class.java))
                return ArtistViewModel(artistRepository) as T
            throw IllegalArgumentException("Model class is not suit")
        }
    }
}