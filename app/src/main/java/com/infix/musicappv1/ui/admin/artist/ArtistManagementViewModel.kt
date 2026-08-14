package com.infix.musicappv1.ui.admin.artist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.infix.musicappv1.data.repository.paging.paging_source.ArtistPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class ArtistManagementViewModel @Inject constructor(
    private val factory: ArtistPagingSource.AssistedFactory
) : ViewModel() {
    data class WrapQuery(
        val query: String,
        val current: Long = System.currentTimeMillis()
    )

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _currentQuery = MutableStateFlow(WrapQuery(""))
    val currentQuery = _currentQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val artists = _currentQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(pageSize = 20, initialLoadSize = 20, prefetchDistance = 5),
            pagingSourceFactory = {
                factory.create(query.query)
            }
        ).flow
    }.cachedIn(viewModelScope)

    fun setQuerySearchState(query: String) {
        _currentQuery.value = WrapQuery(query)
    }
}