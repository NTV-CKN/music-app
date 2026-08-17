package com.infix.musicappv1.data.repository.subscription.payment

import com.infix.musicappv1.data.dto.RequestPaymentResponse
import com.infix.musicappv1.data.source.Result

interface ISubscriptionPaymentRepository {
    suspend fun createPaymentUrl(subscriptionId: String): Result<RequestPaymentResponse>
}