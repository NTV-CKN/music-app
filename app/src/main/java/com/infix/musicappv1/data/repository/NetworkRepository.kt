package com.infix.musicappv1.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

//scope singleton cause now playing and main activity can use NetworkRepository
@Singleton
class NetworkRepository @Inject constructor() {
    private val _hasNetwork = MutableStateFlow<Boolean?>(null)
    val hasNetwork = _hasNetwork.asStateFlow()

    fun setHasNetwork(isNetwork: Boolean) {
        _hasNetwork.value = (isNetwork)
    }
}