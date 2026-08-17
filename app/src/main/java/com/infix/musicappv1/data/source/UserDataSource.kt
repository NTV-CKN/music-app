package com.infix.musicappv1.data.source

import com.infix.musicappv1.data.model.user.User

interface UserDataSource {
    interface Local {
        suspend fun getUserLatest(): User?
        suspend fun insert(user: User)
        suspend fun clear()
    }

    interface Remote {
        suspend fun login(): Result<User>
        suspend fun getCurrentUserVipExpiry(uid: String): Long?
    }
}