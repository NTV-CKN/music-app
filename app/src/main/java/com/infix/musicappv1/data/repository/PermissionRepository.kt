package com.infix.musicappv1.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PermissionRepository {
    //save state permission notification
    private val _isGrantedNotification = MutableStateFlow<Boolean?>(null)
    val isGrantedNotification: StateFlow<Boolean?> = _isGrantedNotification

    //notify for observers active launcher
    private val _askPermissionNotification = MutableStateFlow<Boolean?>(null)
    val askPermissionNotification: StateFlow<Boolean?> = _askPermissionNotification

    fun setAskPermissionNotification(bool: Boolean) {
        _askPermissionNotification.value = bool
    }

    fun setGrantedNotification(bool: Boolean) {
        _isGrantedNotification.value = bool
    }

    companion object {
        @Volatile
        private var instance: PermissionRepository? = null
        fun getInstance(): PermissionRepository {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null)
                        instance = PermissionRepository()
                }
            }
            return instance!!
        }
    }
}