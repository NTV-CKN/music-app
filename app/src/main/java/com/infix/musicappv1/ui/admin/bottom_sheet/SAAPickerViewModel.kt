package com.infix.musicappv1.ui.admin.bottom_sheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.infix.musicappv1.data.repository.paging.paging_source.SongPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

/**
 * A bottom sheet for selecting a Song, Album, or Artist.
 * Supports generic paging for type [T].
 *
 * @param T The type of data displayed in the paged list.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SAAPickerViewModel<T : Any> constructor(
    private val factory: SongPagingSource.AssistedFactory
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
            pagingSourceFactory = { factory.create(query) }
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
}