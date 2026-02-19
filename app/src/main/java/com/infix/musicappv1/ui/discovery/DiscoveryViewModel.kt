package com.infix.musicappv1.ui.discovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.repository.artist.ArtistRepository
import com.infix.musicappv1.data.source.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscoveryViewModel(private val artistRepository: ArtistRepository) : ViewModel() {
    val artists: LiveData<List<Artist>?> = artistRepository.getAllArtists().asLiveData()

    fun loadArtistsRemote() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = artistRepository.loadArtistsRemote()
            if (result is Result.Success) {
                extractArtistNotInDb(result.data)
            }
        }
    }

    private suspend fun extractArtistNotInDb(data: List<Artist>) {
        if (artists.value == null || artists.value!!.isEmpty()) {
            artistRepository.insert(*data.toTypedArray())
        } else {
            val artistNotInDb = mutableListOf<Artist>()
            val setLocal = artists.value!!.toSet()
            for (artist in data)
                if (!setLocal.contains(artist))
                    artistNotInDb.add(artist)

            if (artistNotInDb.isNotEmpty())
                artistRepository.insert(*artistNotInDb.toTypedArray())
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