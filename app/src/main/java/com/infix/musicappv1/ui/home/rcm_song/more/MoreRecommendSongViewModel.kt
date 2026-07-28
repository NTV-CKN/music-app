package com.infix.musicappv1.ui.home.rcm_song.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.infix.musicappv1.data.repository.paging.SongRemoteMediator
import com.infix.musicappv1.data.repository.song.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoreRecommendSongViewModel @Inject constructor(
    private val songRepository: SongRepository,
    factory: SongRemoteMediator.FactoryAssisted
) : ViewModel() {
    @OptIn(ExperimentalPagingApi::class)
    val songs = Pager(
        PagingConfig(initialLoadSize = 20, pageSize = 20, prefetchDistance = 4, enablePlaceholders = false),
        remoteMediator = factory.create(false)
    ) {
        songRepository.getAllSongsPaging()
    }.flow.cachedIn(viewModelScope)
}