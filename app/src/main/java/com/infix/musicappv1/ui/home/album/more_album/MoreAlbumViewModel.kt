package com.infix.musicappv1.ui.home.album.more_album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.infix.musicappv1.data.repository.album.AlbumRepository
import com.infix.musicappv1.data.repository.paging.mediator.AlbumRemoteMediator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoreAlbumViewModel @Inject constructor(
    private val albumRepository: AlbumRepository,
    private val factory: AlbumRemoteMediator.FactoryAssisted
) : ViewModel() {
    @OptIn(ExperimentalPagingApi::class)
    val albums = Pager(
        PagingConfig(initialLoadSize = 20, pageSize = 20),
        remoteMediator = factory.create(false)
    ) {
        albumRepository.loadAlbumsPaging()
    }.flow.cachedIn(viewModelScope)
}