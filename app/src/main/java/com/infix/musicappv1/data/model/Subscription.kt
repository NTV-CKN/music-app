package com.infix.musicappv1.data.model

data class Subscription(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var durationDays: Int = 30,
    var isActive: Boolean,
    var createAt: String = "",
    var updateAt: String = ""
)
