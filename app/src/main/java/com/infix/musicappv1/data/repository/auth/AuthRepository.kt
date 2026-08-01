package com.infix.musicappv1.data.repository.auth

import com.google.firebase.auth.AuthCredential

interface AuthRepository {
    suspend fun loginWithGoogle(
        authCredential: AuthCredential,
        onCompleted: (message: String, isSuccess: Boolean) -> Unit
    )
}