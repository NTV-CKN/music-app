package com.infix.musicappv1.data.source.remote.subscription.payment

import com.infix.musicappv1.data.dto.RequestPaymentResponse
import com.infix.musicappv1.data.source.PaymentDataSource
import com.infix.musicappv1.data.source.remote.MusicService
import retrofit2.Response
import javax.inject.Inject

class SubscriptionPaymentRemoteDS @Inject constructor(
    private val musicService: MusicService
) : PaymentDataSource.Remote {
    override suspend fun createPaymentUrl(subscriptionId: String): Response<RequestPaymentResponse> {
        return musicService.createPaymentUrl(
            mapOf(
                "subscriptionId" to subscriptionId
            )
        )
    }
}