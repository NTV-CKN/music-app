package com.infix.musicappv1.ui.discovery.artist.more

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.infix.musicappv1.data.repository.NetworkRepository
import com.infix.musicappv1.data.repository.artist.ArtistRepository
import com.infix.musicappv1.data.repository.paging.ArtistRemoteMediator
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoreArtistViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
    private val musicDb: MusicDatabase,
    private val networkRepository: NetworkRepository
) : ViewModel() {
    @OptIn(ExperimentalPagingApi::class)
    val artists = Pager(
        PagingConfig(pageSize = 20, initialLoadSize = 20, prefetchDistance = 5),
        remoteMediator = ArtistRemoteMediator(artistRepository, musicDb, networkRepository)
    ) {
        artistRepository.getArtistsPaging()
    }.flow
}