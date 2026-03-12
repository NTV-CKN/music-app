package com.infix.musicappv1.ui.home.rcm_song

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.infix.musicappv1.data.repository.NetworkRepository
import com.infix.musicappv1.data.repository.paging.SongRemoteMediator
import com.infix.musicappv1.data.repository.song.SongRepository
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecommendSongViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val musicDb: MusicDatabase,
    private val networkRepository: NetworkRepository
) : ViewModel() {
    @OptIn(ExperimentalPagingApi::class)
    val songs = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            enablePlaceholders = false
        ),
        remoteMediator = SongRemoteMediator(songRepository, musicDb, networkRepository),
    ) {
        songRepository.getNSongsPaging(SIZE_SONG)
    }.flow.cachedIn(viewModelScope)//cached to avoid refresh when user return app

    companion object {
        const val SIZE_SONG = 10
    }
}