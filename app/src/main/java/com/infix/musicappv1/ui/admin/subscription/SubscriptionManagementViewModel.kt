package com.infix.musicappv1.ui.admin.subscription

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.infix.musicappv1.data.model.Subscription
import com.infix.musicappv1.data.repository.paging.paging_source.SubscriptionPagingSource
import com.infix.musicappv1.data.repository.subscription.SubscriptionRepository
import com.infix.musicappv1.data.source.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SubscriptionManagementViewModel @Inject constructor(
    private val factory: SubscriptionPagingSource.AssistedFactory,
    private val subscriptionRepository: SubscriptionRepository
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
    val subscriptionPagingData = _currentQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                prefetchDistance = 5
            ),
            pagingSourceFactory = { factory.create(query.query) }
        ).flow
    }.cachedIn(viewModelScope)

    fun setSubscriptionPagingState(query: String) {
        _currentQuery.value = WrapQuery(query)
    }

    fun removeSubscription(
        subscription: Subscription,
        callback: (success: Boolean, msg: String) -> Unit
    ) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = subscriptionRepository.removeSubscription(subscription.id)

            withContext(Dispatchers.Main) {
                if (result is Result.Success) {
                    callback.invoke(result.data.success, result.data.message)
                    _currentQuery.value = WrapQuery(_currentQuery.value.query)
                } else if (result is Result.Error) {
                    callback.invoke(false, result.err.message ?: "Unknown error")
                }
                _isLoading.value = false
            }
        }
    }
}