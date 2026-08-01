package com.infix.musicappv1.data.source

import com.google.firebase.auth.AuthCredential

interface AuthDataSource {
    interface Remote {
        suspend fun loginWithGoogle(
            authCredential: AuthCredential,
            onCompleted: (message: String, isSuccess: Boolean) -> Unit
        )
    }
}