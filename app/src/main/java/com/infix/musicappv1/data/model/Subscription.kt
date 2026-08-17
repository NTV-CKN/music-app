package com.infix.musicappv1.data.model

data class Subscription(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var durationDays: Int = 30,
    var isActive: Boolean = true,
    var createAt: String = "",
    var updateAt: String = ""
): java.io.Serializable {
    fun clone(): Subscription {
        return Subscription(
            id,
            name,
            description,
            price,
            durationDays,
            isActive,
            createAt,
            updateAt
        )
    }
}
