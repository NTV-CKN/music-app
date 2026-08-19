package com.infix.musicappv1.data.repository.user

import com.infix.musicappv1.data.model.user.User
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.UserDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val local: UserDataSource.Local,
    private val remote: UserDataSource.Remote
) : IUserRepository{
    override suspend fun getCurrentUserVipExpiry(): Long? {
        val user = local.getUserLatest() ?: return null

        return remote.getCurrentUserVipExpiry(user.uid)
    }

    override suspend fun updateUser(user: User) {
        local.update(user)
    }

    override fun listenerUserDocument(): Flow<Result<User>> = flow{
        val user = local.getUserLatest()
        if(user == null) {
            emit(Result.Error(Exception("Người dùng null")))
            return@flow
        }

        remote.listenerUserDocument(user.uid).collect { result ->
            emit(result)
        }
    }
}