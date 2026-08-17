package com.infix.musicappv1.data.repository.subscription.payment

import com.infix.musicappv1.data.dto.RequestPaymentResponse
import com.infix.musicappv1.data.source.PaymentDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.di.qualifier.SubscriptionPayment
import javax.inject.Inject

class SubscriptionPaymentRepositoryImpl @Inject constructor(
    @param:SubscriptionPayment
    private val remote: PaymentDataSource.Remote
) : ISubscriptionPaymentRepository {
    override suspend fun createPaymentUrl(subscriptionId: String): Result<RequestPaymentResponse> {
        try {
            val response  = remote.createPaymentUrl(subscriptionId)

            if(response.isSuccessful) {
                return Result.Success(
                    response.body()?: throw Exception("Dữ liệu rỗng")
                )
            }

            throw Exception("Tạo đường dẫn thanh toán thất bại")
        }catch (ex: Exception) {
            return Result.Error(ex)
        }
    }
}