package com.infix.musicappv1.data.source.local.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.infix.musicappv1.data.model.user.User

@Dao
interface UserDAO {
    @Query("""
        SELECT *
        FROM users
        ORDER BY login_at DESC
        LIMIT 1
    """)
    suspend fun getUserLatest(): User?

    @Insert
    suspend fun insert(user: User)

    @Query("""
        DELETE
        FROM users
    """)
    suspend fun clear()
}