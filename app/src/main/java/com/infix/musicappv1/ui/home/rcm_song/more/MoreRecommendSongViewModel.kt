package com.infix.musicappv1.ui.home.rcm_song.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.infix.musicappv1.data.repository.song.SongRemoteMediator
import com.infix.musicappv1.data.repository.song.SongRepository
import com.infix.musicappv1.data.source.local.db.MusicDatabase

class MoreRecommendSongViewModel(
    private val songRepository: SongRepository,
    private val musicDb: MusicDatabase
) : ViewModel() {
    @OptIn(ExperimentalPagingApi::class)
    val songs = Pager(
        PagingConfig(initialLoadSize = 20, prefetchDistance = 5, pageSize = 20),
        remoteMediator = SongRemoteMediator(songRepository, musicDb)
    ) {
        songRepository.getAllSongsPaging()
    }.flow


    class Factory(private val songRepository: SongRepository, private val musicDb: MusicDatabase) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MoreRecommendSongViewModel::class.java))
                return MoreRecommendSongViewModel(songRepository, musicDb) as T
            throw IllegalArgumentException("Model class is not suit")
        }
    }

}