package com.infix.musicappv1.ui.admin.bottom_sheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.infix.musicappv1.data.repository.paging.paging_source.AlbumPagingSource
import com.infix.musicappv1.data.repository.paging.paging_source.ArtistPagingSource
import com.infix.musicappv1.data.repository.paging.paging_source.SongPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

/**
 * A bottom sheet for selecting a Song, Album, or Artist.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SAAPickerViewModel @Inject constructor(
    private val factorySong: SongPagingSource.AssistedFactory,
    private val factoryAlbum: AlbumPagingSource.AssistedFactory,
    private val factoryArtist: ArtistPagingSource.AssistedFactory,
) : ViewModel() {
    enum class TypeSAAPicker {
        SONG, ALBUM, ARTIST
    }

    private val connect: MutableMap<TypeSAAPicker, String> = mutableMapOf()

    private val _currentSongQuery = MutableStateFlow("")
    private val _currentAlbumQuery = MutableStateFlow("")
    private val _currentArtistQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val songPagingData = _currentSongQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                prefetchDistance = 5
            ),
            pagingSourceFactory = { factorySong.create(query) }
        ).flow
    }.cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val albumPagingData = _currentAlbumQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                prefetchDistance = 5
            ),
            pagingSourceFactory = { factoryAlbum.create(query) }
        ).flow
    }.cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val artistPagingData = _currentArtistQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                prefetchDistance = 5
            ),
            pagingSourceFactory = { factoryArtist.create(query) }
        ).flow
    }.cachedIn(viewModelScope)

    init {
        //song
        connect[TypeSAAPicker.SONG] = _currentSongQuery.value
        //album
        connect[TypeSAAPicker.ALBUM] = _currentAlbumQuery.value
        //artist
        connect[TypeSAAPicker.ARTIST] = _currentArtistQuery.value
    }

    fun getCurrentQuery(type: TypeSAAPicker): String {
        if (connect[type] != null)
            return connect[type]!!

        return "";
    }

    fun setQuerySearchState(str: String, type: TypeSAAPicker) {
        if (connect[type] == null || connect[type] == str) return

        connect[type] = str
        when (type) {
            TypeSAAPicker.SONG -> _currentSongQuery.value = str
            TypeSAAPicker.ALBUM -> _currentAlbumQuery.value = str
            TypeSAAPicker.ARTIST -> _currentArtistQuery.value = str
        }
    }
}