package com.infix.musicappv1.data.source.remote.user

import com.infix.musicappv1.data.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.POST

interface UserService {
    @POST("v1/auth/login")
    suspend fun login(): Response<LoginResponse>
}