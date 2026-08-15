package com.infix.musicappv1.data.model.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val uid: String,
    @ColumnInfo("email")
    val email: String,
    @ColumnInfo("display_name")
    val displayName: String,
    @ColumnInfo("avatar")
    val avatar: String,
    @ColumnInfo("role")
    val role: String,
    @ColumnInfo("login_at")
    val loginAt: Long = System.currentTimeMillis(),
    val isVip: Boolean = false,
    val vipExpiryDate: String
) : Serializable
