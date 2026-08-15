package com.infix.musicappv1.data.source

import com.infix.musicappv1.data.dto.BaseResultResponse
import com.infix.musicappv1.data.model.Subscription
import com.infix.musicappv1.data.model.SubscriptionList
import retrofit2.Response

interface SubscriptionDataSource {
    interface Remote {
        suspend fun loadSubscriptions(query: String, limit: Int, key: Int): SubscriptionList?
        suspend fun saveSubscription(subscription: Subscription): Response<BaseResultResponse>
        suspend fun updateSubscription(subscription: Subscription): Response<BaseResultResponse>
        suspend fun removeSubscription(body: Map<String, String>): Response<BaseResultResponse>
    }
}
