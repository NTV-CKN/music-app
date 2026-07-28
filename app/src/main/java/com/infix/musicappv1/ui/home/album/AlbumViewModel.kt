package com.infix.musicappv1.ui.home.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.infix.musicappv1.data.repository.album.AlbumRepository
import com.infix.musicappv1.data.repository.paging.AlbumRemoteMediator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val albumRepository: AlbumRepository,
    private val factory: AlbumRemoteMediator.FactoryAssisted
) : ViewModel() {
    @OptIn(ExperimentalPagingApi::class)
    val albums = Pager(
        PagingConfig(initialLoadSize = 20, pageSize = 20, prefetchDistance = 5),
        remoteMediator = factory.create(true)
    ) {
        albumRepository.loadNAlbumPaging(10)
    }.flow.cachedIn(viewModelScope)
}