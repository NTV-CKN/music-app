package com.infix.musicappv1.data.repository.auth

import com.google.firebase.auth.AuthCredential
import com.infix.musicappv1.data.model.user.User

interface AuthRepository {
    suspend fun loginWithGoogle(
        authCredential: AuthCredential,
        onCompleted: (message: String, isSuccess: Boolean) -> Unit
    )

    suspend fun getUserSession(): User?

    suspend fun logout()
}