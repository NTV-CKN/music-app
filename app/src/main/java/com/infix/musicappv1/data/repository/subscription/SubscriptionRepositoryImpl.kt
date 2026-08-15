package com.infix.musicappv1.data.repository.subscription

import com.infix.musicappv1.data.dto.BaseResultResponse
import com.infix.musicappv1.data.model.Subscription
import com.infix.musicappv1.data.model.SubscriptionList
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.SubscriptionDataSource
import com.infix.musicappv1.utils.GenerateIdHelper
import javax.inject.Inject

class SubscriptionRepositoryImpl @Inject constructor(
    private val remote: SubscriptionDataSource.Remote
) : SubscriptionRepository {

    override suspend fun loadSubscriptionsPaging(query: String, limit: Int, key: Int): SubscriptionList? {
        return remote.loadSubscriptions(query, limit, key)
    }

    override suspend fun saveSubscription(subscription: Subscription): Result<BaseResultResponse> {
        return try {
            if (subscription.id.isBlank()) {
                subscription.id = GenerateIdHelper.generateId()
            }

            val response = remote.saveSubscription(subscription)
            if (response.isSuccessful) {
                Result.Success(
                    response.body() ?: BaseResultResponse(true, "Thêm gói đăng ký thành công")
                )
            } else {
                throw Exception("Lưu gói đăng ký thất bại")
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateSubscription(subscription: Subscription): Result<BaseResultResponse> {
        return try {
            val response = remote.updateSubscription(subscription)
            if (response.isSuccessful) {
                Result.Success(
                    response.body() ?: BaseResultResponse(true, "Cập nhật gói đăng ký thành công")
                )
            } else {
                throw Exception("Cập nhật gói đăng ký thất bại")
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun removeSubscription(id: String): Result<BaseResultResponse> {
        return try {
            val response = remote.removeSubscription(mapOf("id" to id))
            if (response.isSuccessful) {
                Result.Success(
                    response.body() ?: BaseResultResponse(true, "Xóa gói đăng ký thành công")
                )
            } else {
                throw Exception("Xóa gói đăng ký thất bại")
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
