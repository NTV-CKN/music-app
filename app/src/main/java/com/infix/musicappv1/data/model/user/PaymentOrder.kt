package com.infix.musicappv1.data.model.user

import com.google.gson.annotations.SerializedName
import com.infix.musicappv1.data.model.Subscription

data class PaymentOrder(
    @SerializedName("orderId")
    var orderId: String = "",

    @SerializedName("userId")
    var userId: String = "",

    @SerializedName("paymentMethod")
    var paymentMethod: String = "",

    @SerializedName("status")
    var status: String = "",

    @SerializedName("amount")
    var amount: Double = 0.0,

    @SerializedName("vnpayResponseCode")
    var vnpayResponseCode: String = "",

    @SerializedName("vnpayTransactionNo")
    var vnpayTransactionNo: String = "",

    @SerializedName("purchasedAt")
    var purchasedAt: String = "",

    @SerializedName("previousExpiryDate")
    var previousExpiryDate: String = "",

    @SerializedName("newExpiryDate")
    var newExpiryDate: String = "",

    @SerializedName("packageSnapshot")
    var subscription: Subscription = Subscription(isActive = true)
)
