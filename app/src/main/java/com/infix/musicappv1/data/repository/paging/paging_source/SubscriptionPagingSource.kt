package com.infix.musicappv1.data.repository.paging.paging_source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.infix.musicappv1.data.model.Subscription
import com.infix.musicappv1.data.repository.subscription.SubscriptionRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class SubscriptionPagingSource @AssistedInject constructor(
    private val subscriptionRepository: SubscriptionRepository,
    @Assisted
    private val query: String,
) : PagingSource<Int, Subscription>() {

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(query: String): SubscriptionPagingSource
    }

    override fun getRefreshKey(state: PagingState<Int, Subscription>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Subscription> {
        try {
            val key = params.key ?: 0
            val limit = params.loadSize

            val subscriptionList = subscriptionRepository.loadSubscriptionsPaging(query, limit, key)?.subscriptions ?: emptyList()

            Log.d("SubscriptionPagingSource", "Load: $subscriptionList")

            return LoadResult.Page(
                subscriptionList,
                prevKey = if (key == 0) null else key.minus(1),
                nextKey = if (subscriptionList.isEmpty() || subscriptionList.size < params.loadSize) null else key.plus(1)
            )
        } catch (ex: Exception) {
            return LoadResult.Error(ex)
        }
    }
}
