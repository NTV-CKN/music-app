package com.infix.musicappv1.data.model

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class Subscription(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var durationDays: Int = 30,
    var isActive: Boolean,
    var createAt: String = "",
    var updateAt: String = ""
) {

    //tmp
    fun String.toFormattedDate(): String {
        return try {
            val instant = Instant.parse(this)
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                .withZone(ZoneId.systemDefault())
            formatter.format(instant)
        } catch (e: Exception) {
            this
        }
    }
}
