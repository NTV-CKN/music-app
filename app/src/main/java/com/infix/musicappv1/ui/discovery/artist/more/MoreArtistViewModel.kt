package com.infix.musicappv1.ui.discovery.artist.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.infix.musicappv1.data.repository.artist.ArtistRepository
import com.infix.musicappv1.data.repository.paging.ArtistRemoteMediator
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.ui.discovery.artist.ArtistViewModel.Companion.ARTIST_SIZE

class MoreArtistViewModel(
    private val artistRepository: ArtistRepository,
    private val musicDb: MusicDatabase
) : ViewModel() {
    @OptIn(ExperimentalPagingApi::class)
    val artists = Pager(
        PagingConfig(pageSize = 20, initialLoadSize = 20, prefetchDistance = 5),
        remoteMediator = ArtistRemoteMediator(artistRepository, musicDb)
    ) {
        artistRepository.getArtistsPaging()
    }.flow

    class Factory(
        private val artistRepository: ArtistRepository,
        private val musicDb: MusicDatabase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MoreArtistViewModel::class.java))
                return MoreArtistViewModel(artistRepository, musicDb) as T
            throw IllegalArgumentException("Model class is not suit")
        }
    }
}