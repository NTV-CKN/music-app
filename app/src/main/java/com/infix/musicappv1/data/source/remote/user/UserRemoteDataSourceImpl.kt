package com.infix.musicappv1.data.source.remote.user

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.infix.musicappv1.data.model.user.User
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.UserDataSource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val userService: UserService,
    private val firestore: FirebaseFirestore
) : UserDataSource.Remote {
    override suspend fun login(): Result<User> {
        try {
            val loginResponse = userService.login()
            if (loginResponse.isSuccessful) {
                val body = loginResponse.body()
                return if (body != null) {
                    Log.d("UserRemoteDataSourceImpl", body.user.toString() ?: " null")
                    Result.Success(body.user)
                }
                else
                    Result.Error(Exception("Sign in failed! Body is null"))
            }

            return Result.Error(Exception("Sign in failed! Request is not successful"))
        } catch (ex: Exception) {
            return Result.Error(Exception("Sign in failed: ${ex.message}"))
        }
    }

    override suspend fun getCurrentUserVipExpiry(uid: String): Long? {
        try {
            val userSnapshot =  firestore.collection("users").document(uid).get().await()
            val vipExpiryDate = userSnapshot.getTimestamp("vipExpiryDate")

            return vipExpiryDate?.toDate()?.time
        }catch (ex: Exception) {
            Log.e("UserRemoteDataSourceImpl", ex.message?:"Unknown")
            return null
        }
    }
}