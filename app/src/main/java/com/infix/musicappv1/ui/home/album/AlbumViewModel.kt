package com.infix.musicappv1.ui.home.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.infix.musicappv1.data.repository.album.AlbumRepository
import com.infix.musicappv1.data.repository.paging.AlbumRemoteMediator
import com.infix.musicappv1.data.source.local.db.MusicDatabase

class AlbumViewModel(
    private val albumRepository: AlbumRepository,
    private val musicDb: MusicDatabase
) : ViewModel() {
    @OptIn(ExperimentalPagingApi::class)
    val albums = Pager(
        PagingConfig(initialLoadSize = 20, pageSize = 20, prefetchDistance = 5),
        remoteMediator = AlbumRemoteMediator(albumRepository, musicDb)
    ) {
        albumRepository.loadNAlbumPaging(10)
    }.flow.cachedIn(viewModelScope)

    class Factory(
        private val albumRepository: AlbumRepository,
        private val musicDb: MusicDatabase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumViewModel::class.java))
                return AlbumViewModel(albumRepository, musicDb) as T
            throw IllegalArgumentException("Model class is not suit")
        }
    }
}