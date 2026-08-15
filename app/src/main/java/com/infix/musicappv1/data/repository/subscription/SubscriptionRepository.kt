package com.infix.musicappv1.data.repository.subscription

import com.infix.musicappv1.data.dto.BaseResultResponse
import com.infix.musicappv1.data.model.Subscription
import com.infix.musicappv1.data.model.SubscriptionList
import com.infix.musicappv1.data.source.Result

interface SubscriptionRepository {
    suspend fun loadSubscriptionsPaging(query: String, limit: Int, key: Int): SubscriptionList?
    suspend fun saveSubscription(subscription: Subscription): Result<BaseResultResponse>
    suspend fun updateSubscription(subscription: Subscription): Result<BaseResultResponse>
    suspend fun removeSubscription(id: String): Result<BaseResultResponse>
}
