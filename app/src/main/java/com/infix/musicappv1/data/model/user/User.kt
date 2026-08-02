package com.infix.musicappv1.data.model.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    private val uid: String,
    @ColumnInfo("email")
    private val email: String,
    @ColumnInfo("display_name")
    private val displayName: String,
    @ColumnInfo("avatar")
    private val avatar: String,
    @ColumnInfo("role")
    private val role: String,
    @ColumnInfo("login_at")
    private val loginAt: Long = System.currentTimeMillis()
)
