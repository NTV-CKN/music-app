package com.infix.musicappv1.data.source.remote.user

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.infix.musicappv1.data.dto.UserDTO
import com.infix.musicappv1.data.model.user.User
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.UserDataSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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
                } else
                    Result.Error(Exception("Sign in failed! Body is null"))
            }

            return Result.Error(Exception("Sign in failed! Request is not successful"))
        } catch (ex: Exception) {
            return Result.Error(Exception("Sign in failed: ${ex.message}"))
        }
    }

    override suspend fun getCurrentUserVipExpiry(uid: String): Long? {
        try {
            val userSnapshot = firestore.collection("users").document(uid).get().await()
            val vipExpiryDate = userSnapshot.getTimestamp("vipExpiryDate")

            return vipExpiryDate?.toDate()?.time
        } catch (ex: Exception) {
            Log.e("UserRemoteDataSourceImpl", ex.message ?: "Unknown")
            return null
        }
    }

    override fun listenerUserDocument(uid: String): Flow<Result<User>> = callbackFlow {
        val listenerRegistration =
            firestore.collection("users").document(uid).addSnapshotListener { snapshot, error ->
                try {
                    if (error != null) {
                        throw Exception(error)
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val userDto = snapshot.toObject(UserDTO::class.java)
                        val userEntity = userDto?.toEntity()
                            ?: throw Exception("Dữ liệu người dùng null")


                        trySend(Result.Success(userEntity))
                    } else
                        throw Exception("Dữ liệu người dùng không tồn tại")
                } catch (ex: Exception) {
                    trySend(Result.Error(ex))
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }
}