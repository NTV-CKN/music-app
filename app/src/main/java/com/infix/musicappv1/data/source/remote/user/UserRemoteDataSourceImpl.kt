package com.infix.musicappv1.data.source.remote.user

import com.infix.musicappv1.data.model.user.User
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.UserDataSource
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val userService: UserService
) : UserDataSource.Remote {
    override suspend fun login(): Result<User> {
        try {
            val loginResponse = userService.login()
            if (loginResponse.isSuccessful) {
                val body = loginResponse.body()
                return if (body != null)
                    Result.Success(body.user)
                else
                    Result.Error(Exception("Sign in failed! Body is null"))
            }

            return Result.Error(Exception("Sign in failed! Request is not successful"))
        } catch (ex: Exception) {
            return Result.Error(Exception("Sign in failed: ${ex.message}"))
        }
    }
}