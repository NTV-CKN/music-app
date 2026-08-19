package com.infix.musicappv1.data.source.local.user

import com.infix.musicappv1.data.model.user.User
import com.infix.musicappv1.data.source.UserDataSource
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    private val userDAO: UserDAO
) : UserDataSource.Local {
    override suspend fun getUserLatest(): User? {
        return userDAO.getUserLatest()
    }

    override suspend fun insert(user: User) {
        userDAO.insert(user)
    }

    override suspend fun update(user: User) {
        userDAO.update(user)
    }

    override suspend fun clear() {
        userDAO.clear()
    }
}