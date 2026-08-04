package com.infix.musicappv1.data.repository.auth

import com.google.firebase.auth.AuthCredential
import com.infix.musicappv1.data.model.user.User
import com.infix.musicappv1.data.source.AuthDataSource
import com.infix.musicappv1.data.source.UserDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remote: AuthDataSource.Remote,
    private val userLocalDataSource: UserDataSource.Local
): AuthRepository {
    override suspend fun loginWithGoogle(
        authCredential: AuthCredential,
        onCompleted: (message: String, isSuccess: Boolean) -> Unit
    ) = remote.loginWithGoogle(
        authCredential,
        onCompleted
    )

    override suspend fun getUserSession(): User? {
        return remote.getUserSession()
    }

    override suspend fun logout() {
        remote.logout()
        userLocalDataSource.clear()
    }
}