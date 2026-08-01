package com.infix.musicappv1.data.source.remote.auth

import android.content.Context
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.infix.musicappv1.R
import com.infix.musicappv1.data.source.AuthDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    @param:ApplicationContext
    private val context: Context
) : AuthDataSource.Remote {
    override suspend fun loginWithGoogle(
        authCredential: AuthCredential,
        onCompleted: (message: String, isSuccess: Boolean) -> Unit
    ) {
        try {
            val authResult = auth.signInWithCredential(authCredential).await()
            val user = authResult.user

            if (user != null) {
                val tokenResult = user.getIdToken(false).await()
                val idToken = tokenResult.token

                if (!idToken.isNullOrEmpty()) {
                    onCompleted(
                        context.getString(
                            R.string.txt_login_success_args, user.displayName
                        ), true
                    )
                } else {
                    onCompleted(
                        context.getString(
                            R.string.txt_error_id_token_firebase
                        ), false
                    )
                }
            } else {
                onCompleted(
                    context.getString(
                        R.string.txt_login_failed
                    ), false
                )
            }

        } catch (e: Exception) {
            val errorMessage = e.localizedMessage ?: "Unknown Error"
            onCompleted(errorMessage, false)
        }
    }
}