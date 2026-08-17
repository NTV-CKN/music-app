package com.infix.musicappv1.data.repository.user

import com.infix.musicappv1.data.source.UserDataSource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val local: UserDataSource.Local,
    private val remote: UserDataSource.Remote
) : IUserRepository{
    override suspend fun getCurrentUserVipExpiry(): Long? {
        val user = local.getUserLatest() ?: return null

        return remote.getCurrentUserVipExpiry(user.uid)
    }
}