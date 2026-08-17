package com.infix.musicappv1.data.repository.user

interface IUserRepository {
    suspend fun getCurrentUserVipExpiry(): Long?
}