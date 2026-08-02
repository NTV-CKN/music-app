package com.infix.musicappv1.data.dto

import com.google.gson.annotations.SerializedName
import com.infix.musicappv1.data.model.user.User

data class LoginResponse(
    val success: Boolean,
    val message: String,
    @SerializedName("userData")
    val user: User
)