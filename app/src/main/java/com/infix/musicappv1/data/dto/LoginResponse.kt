package com.infix.musicappv1.data.dto

import com.infix.musicappv1.data.model.user.User
import kotlinx.serialization.SerialName

data class LoginResponse(
    val success: Boolean,
    val message: String,
    @SerialName("userData")
    val user: User
)