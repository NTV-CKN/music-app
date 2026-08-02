package com.infix.musicappv1.data.source.remote.auth

import android.content.Context
import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.infix.musicappv1.R
import com.infix.musicappv1.data.source.AuthDataSource
import com.infix.musicappv1.data.source.UserDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    @param:ApplicationContext
    private val context: Context,
    private val userRemoteDataSource: UserDataSource.Remote,
    private val userLocalDataSource: UserDataSource.Local
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
                    val result = userRemoteDataSource.login()
                    if(result is com.infix.musicappv1.data.source.Result.Error)
                        throw Exception(result.err)
                    else if(result is com.infix.musicappv1.data.source.Result.Success){
                        Log.d("AuthRemoteDataSourceImpl", result.data.toString())
                        userLocalDataSource.clear()
                        userLocalDataSource.insert(result.data)
                    }

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
            val errorMessage = e.message ?: "Unknown Error"
            Log.d("AuthRemoteDataSourceImpl", errorMessage)
            onCompleted(errorMessage, false)
        }
    }
}