package com.infix.musicappv1.data.repository.user

import com.infix.musicappv1.data.model.user.User
import com.infix.musicappv1.data.source.Result
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun getCurrentUserVipExpiry(): Long?
    suspend fun updateUser(user: User)
    fun listenerUserDocument(): Flow<Result<User>>
}