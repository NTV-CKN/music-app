package com.infix.musicappv1.data.source.remote.subscription

import com.infix.musicappv1.data.dto.BaseResultResponse
import com.infix.musicappv1.data.model.Subscription
import com.infix.musicappv1.data.model.SubscriptionList
import com.infix.musicappv1.data.source.SubscriptionDataSource
import com.infix.musicappv1.data.source.remote.MusicService
import retrofit2.Response
import javax.inject.Inject

class SubscriptionRemoteDataSource @Inject constructor(
    private val musicService: MusicService
) : SubscriptionDataSource.Remote {

    override suspend fun loadSubscriptions(
        query: String,
        limit: Int,
        key: Int
    ): SubscriptionList? {
        val result = musicService.loadSubscriptionsPaging(query, limit, key)
        return if (result.isSuccessful) {
            result.body() ?: SubscriptionList()
        } else {
            SubscriptionList()
        }
    }

    override suspend fun saveSubscription(subscription: Subscription): Response<BaseResultResponse> {
        return musicService.saveSubscription(subscription)
    }

    override suspend fun updateSubscription(subscription: Subscription): Response<BaseResultResponse> {
        return musicService.updateSubscription(subscription)
    }

    override suspend fun removeSubscription(body: Map<String, String>): Response<BaseResultResponse> {
        return musicService.removeSubscription(body)
    }
}
