package com.infix.musicappv1.data.repository.auth

import com.google.firebase.auth.AuthCredential
import com.infix.musicappv1.data.source.AuthDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remote: AuthDataSource.Remote
): AuthRepository {
    override suspend fun loginWithGoogle(
        authCredential: AuthCredential,
        onCompleted: (message: String, isSuccess: Boolean) -> Unit
    ) = remote.loginWithGoogle(
        authCredential,
        onCompleted
    )
}