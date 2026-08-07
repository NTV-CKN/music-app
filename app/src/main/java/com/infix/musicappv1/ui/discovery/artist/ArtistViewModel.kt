package com.infix.musicappv1.ui.discovery.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.repository.artist.ArtistRepository
import com.infix.musicappv1.data.repository.paging.mediator.ArtistRemoteMediator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
    private val factory: ArtistRemoteMediator.FactoryAssisted
) : ViewModel() {
    @OptIn(ExperimentalPagingApi::class)
    val artists = Pager(
        PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 5,
            enablePlaceholders = false
        ),
        remoteMediator = factory.create(true)
    ) {
        artistRepository.getNArtistsPaging(ARTIST_SIZE)
    }.flow.cachedIn(viewModelScope)

    fun updateInterestedArtist(artist: Artist) {
        viewModelScope.launch(Dispatchers.IO) {
            artistRepository.update(artist)
        }
    }

    companion object {
        const val ARTIST_SIZE = 10
    }
}