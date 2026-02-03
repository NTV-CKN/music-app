package com.infix.musicappv1.data.source

sealed class Result<T> {
    data class Success<T>(val data: T): Result<T>()
    data class Error<T>(val err: Exception): Result<T>()
}