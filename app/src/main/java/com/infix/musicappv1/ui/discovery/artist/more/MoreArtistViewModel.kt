package com.infix.musicappv1.ui.discovery.artist.more

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.infix.musicappv1.data.repository.artist.ArtistRepository
import com.infix.musicappv1.data.repository.paging.ArtistRemoteMediator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoreArtistViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
    private val factory: ArtistRemoteMediator.FactoryAssisted
) : ViewModel() {
    @OptIn(ExperimentalPagingApi::class)
    val artists = Pager(
        PagingConfig(pageSize = 20, initialLoadSize = 20, prefetchDistance = 5),
        remoteMediator = factory.create(false)
    ) {
        artistRepository.getArtistsPaging()
    }.flow
}