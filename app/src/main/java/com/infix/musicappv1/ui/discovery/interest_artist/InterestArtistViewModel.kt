package com.infix.musicappv1.ui.discovery.interest_artist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.repository.artist.ArtistRepository

class InterestArtistViewModel(
    private val artistRepository: ArtistRepository
) : ViewModel() {
    val artistsInterested: LiveData<List<Artist>> = artistRepository.getLimitInterestArtist().asLiveData()

    class Factory(
        private val artistRepository: ArtistRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InterestArtistViewModel::class.java))
                return InterestArtistViewModel(artistRepository) as T
            throw IllegalArgumentException("Model class is not suit")
        }
    }
}