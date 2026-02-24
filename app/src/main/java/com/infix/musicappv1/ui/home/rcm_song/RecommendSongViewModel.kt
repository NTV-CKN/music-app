package com.infix.musicappv1.ui.home.rcm_song

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.infix.musicappv1.data.repository.song.SongRemoteMediator
import com.infix.musicappv1.data.repository.song.SongRepository
import com.infix.musicappv1.data.source.local.db.MusicDatabase

class RecommendSongViewModel(
    private val songRepository: SongRepository,
    private val musicDb: MusicDatabase
) : ViewModel() {
    //    private val _songs = MutableLiveData<List<Song>>()
//    val songs: LiveData<List<Song>> = _songs
    @OptIn(ExperimentalPagingApi::class)
    val songs = Pager(
        config = PagingConfig(
            pageSize = 10,
            initialLoadSize = 10,
            prefetchDistance = 3,
            enablePlaceholders = false
        ),
        remoteMediator = SongRemoteMediator(songRepository, musicDb),
    ) {
        songRepository.getNSongsPaging(10)
    }.flow.cachedIn(viewModelScope)//cached to avoid refresh when user return app


    class Factory(private val songRepository: SongRepository, private val musicDb: MusicDatabase) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecommendSongViewModel::class.java))
                return RecommendSongViewModel(songRepository, musicDb) as T
            throw IllegalArgumentException("Model class is not suit")
        }
    }
}