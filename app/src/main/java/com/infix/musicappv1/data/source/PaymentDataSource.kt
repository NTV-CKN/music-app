package com.infix.musicappv1.data.source

import com.infix.musicappv1.data.dto.RequestPaymentResponse
import retrofit2.Response

interface PaymentDataSource {
    interface Remote {
        suspend fun createPaymentUrl(subscriptionId: String): Response<RequestPaymentResponse>
    }
}