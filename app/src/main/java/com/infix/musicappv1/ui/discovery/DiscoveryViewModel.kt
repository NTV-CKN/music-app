package com.infix.musicappv1.ui.discovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.repository.artist.ArtistRepository
import com.infix.musicappv1.data.source.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscoveryViewModel(private val artistRepository: ArtistRepository) : ViewModel() {
    private val _artists = MutableLiveData<List<Artist>?>()
    val artists: LiveData<List<Artist>?> = _artists

    fun loadArtistsRemote() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = artistRepository.loadArtistsRemote()
            if (result is Result.Success) {
                _artists.postValue(result.data)
            } else if (result is Result.Error) {
                _artists.postValue(emptyList())
            }
        }
    }

    class Factory(private val artistRepository: ArtistRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DiscoveryViewModel::class.java))
                return DiscoveryViewModel(artistRepository) as T
            throw IllegalArgumentException("Model class is not suit")
        }
    }
}