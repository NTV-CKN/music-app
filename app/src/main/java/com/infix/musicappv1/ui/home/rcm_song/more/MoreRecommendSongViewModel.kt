package com.infix.musicappv1.ui.home.rcm_song.more

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.infix.musicappv1.data.repository.NetworkRepository
import com.infix.musicappv1.data.repository.paging.SongRemoteMediator
import com.infix.musicappv1.data.repository.song.SongRepository
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoreRecommendSongViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val musicDb: MusicDatabase,
    private val networkRepository: NetworkRepository
) : ViewModel() {
    @OptIn(ExperimentalPagingApi::class)
    val songs = Pager(
        PagingConfig(initialLoadSize = 20, pageSize = 20, enablePlaceholders = false),
        remoteMediator = SongRemoteMediator(songRepository, musicDb, networkRepository)
    ) {
        songRepository.getAllSongsPaging()
    }.flow


//    class Factory(private val songRepository: SongRepository, private val musicDb: MusicDatabase) :
//        ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(MoreRecommendSongViewModel::class.java))
//                return MoreRecommendSongViewModel(songRepository, musicDb) as T
//            throw IllegalArgumentException("Model class is not suit")
//        }
//    }

}