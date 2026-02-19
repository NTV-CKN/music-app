package com.infix.musicappv1.ui.discovery.artist.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.artist.ArtistWithSongs
import com.infix.musicappv1.data.repository.artist.ArtistRepository
import com.infix.musicappv1.ui.discovery.DiscoveryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtistDetailViewModel(
    private val artistRepository: ArtistRepository
) : ViewModel() {
    private val _artistWithSongs = MutableLiveData<ArtistWithSongs?>()
    val artistWithSongs: LiveData<ArtistWithSongs?> = _artistWithSongs

    fun setArtistWithSongsByArtistId(artistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _artistWithSongs.postValue(artistRepository.getArtistWithSongsByArtistId(artistId))
        }
    }

    class Factory(
        private val artistRepository: ArtistRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistDetailViewModel::class.java))
                return ArtistDetailViewModel(artistRepository) as T
            throw IllegalArgumentException("Model class is not suit")
        }
    }
}