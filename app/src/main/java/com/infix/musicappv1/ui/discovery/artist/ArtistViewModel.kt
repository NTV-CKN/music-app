package com.infix.musicappv1.ui.discovery.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.repository.artist.ArtistRepository
import com.infix.musicappv1.data.repository.paging.ArtistRemoteMediator
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtistViewModel(
    private val artistRepository: ArtistRepository,
    private val musicDb: MusicDatabase
) : ViewModel() {
    @OptIn(ExperimentalPagingApi::class)
    val artists = Pager(
        PagingConfig(pageSize = 20, initialLoadSize = 20, prefetchDistance = 5, enablePlaceholders = false),
        remoteMediator = ArtistRemoteMediator(artistRepository, musicDb)
    ) {
        artistRepository.getNArtistsPaging(ARTIST_SIZE)
    }.flow.cachedIn(viewModelScope)

    fun updateInterestedArtist(artist: Artist) {
        viewModelScope.launch(Dispatchers.IO) {
            artistRepository.update(artist)
        }
    }

    class Factory(
        private val artistRepository: ArtistRepository,
        private val musicDb: MusicDatabase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistViewModel::class.java))
                return ArtistViewModel(artistRepository, musicDb) as T
            throw IllegalArgumentException("Model class is not suit")
        }
    }

    companion object {
        const val ARTIST_SIZE = 10
    }
}