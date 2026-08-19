package com.infix.musicappv1.data.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.infix.musicappv1.data.model.user.User
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

data class UserDTO(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val avatar: String = "",
    val role: String = "user",
    @get:PropertyName("isVip")
    val isVip: Boolean = false,
    val createAt: Timestamp? = null,
    val updateAt: Timestamp? = null,
    val vipExpiryDate: Timestamp? = null
) {
    fun toEntity(): User {
        return User(
            uid = uid,
            email = email,
            displayName = displayName,
            avatar = avatar,
            role = role,
            isVip = isVip,
            vipExpiryDate = vipExpiryDate?.toIso8601String()
        )
    }

    fun Timestamp.toIso8601String(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        return sdf.format(this.toDate())
    }
}