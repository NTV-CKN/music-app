package com.infix.musicappv1.ui.discovery.artist.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.repository.artist.ArtistRepository

class MoreArtistViewModel(private val artistRepository: ArtistRepository) : ViewModel() {
    val artists: LiveData<List<Artist>?> = artistRepository.getAllArtists().asLiveData()

    class Factory(private val artistRepository: ArtistRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MoreArtistViewModel::class.java))
                return MoreArtistViewModel(artistRepository) as T
            throw IllegalArgumentException("Model class is not suit")
        }
    }
}