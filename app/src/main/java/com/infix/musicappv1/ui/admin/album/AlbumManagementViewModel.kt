package com.infix.musicappv1.ui.admin.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.infix.musicappv1.data.repository.paging.paging_source.AlbumPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class AlbumManagementViewModel @Inject constructor(
    private val factory: AlbumPagingSource.AssistedFactory
) : ViewModel() {
    private val _currentQuery = MutableStateFlow("")
    val currentQuery = _currentQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val albums = _currentQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(pageSize = 20, initialLoadSize = 20, prefetchDistance = 5),
            pagingSourceFactory = {
                factory.create(query)
            }
        ).flow
    }.cachedIn(viewModelScope)

    fun setQuerySearchState(query: String) {
        _currentQuery.value = query
    }
}