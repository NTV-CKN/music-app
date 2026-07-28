package com.infix.musicappv1.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.album.AlbumRepository
import com.infix.musicappv1.data.repository.paging.SongRemoteMediator
import com.infix.musicappv1.data.repository.song.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
@Suppress("UNCHECKED_CAST")
class HomeViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val albumRepository: AlbumRepository,
    factory: SongRemoteMediator.FactoryAssisted
) : ViewModel() {
    //when any fragment has paging song, room update more song and notify for songLocal
    val songLocal: LiveData<List<Song>?> = songRepository.getAllSongsFlow().asLiveData()
    val albumLocal: LiveData<List<Album>?> = albumRepository.loadAllAlbumsFlow().asLiveData()

    @OptIn(ExperimentalPagingApi::class)
    val songs = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 1,
            enablePlaceholders = false
        ),
        remoteMediator = factory.create(true),
    ) {
        songRepository.getNSongsPaging(SIZE_SONG)
    }.flow.cachedIn(viewModelScope)//cached to avoid refresh when user return app

    companion object {
        const val SIZE_SONG = 10
    }
}