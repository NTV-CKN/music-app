package com.infix.musicappv1.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.infix.musicappv1.data.model.user.User
import com.infix.musicappv1.data.repository.PlaybackRepository
import com.infix.musicappv1.data.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val playbackRepository: PlaybackRepository
) : ViewModel() {
    data class WrapUserSession(
        val user: User? = null,
        val time: Long = System.currentTimeMillis()
    )

    private val _isLoading: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isLoading: SharedFlow<Boolean> = _isLoading

    private val _userSession: MutableStateFlow<WrapUserSession> = MutableStateFlow(WrapUserSession())
    val userSession: StateFlow<WrapUserSession> = _userSession

    fun emitLoading(isLoading: Boolean) {
        viewModelScope.launch { _isLoading.emit(isLoading) }
    }

    fun loadUserSession() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            _userSession.emit(
                WrapUserSession(authRepository.getUserSession())
            )
            _isLoading.emit(false)
        }
    }

    fun loginWithGoogle(
        authCredential: AuthCredential,
        onCompleted: (message: String, isSuccess: Boolean) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)

            authRepository.loginWithGoogle(
                authCredential
            ) { message, isSuccess ->
                onCompleted.invoke(message, isSuccess)
                playbackRepository.invokeRefreshHttpDataSource()
            }

            _isLoading.emit(false)
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.logout()
            playbackRepository.invokeRefreshHttpDataSource()
        }
    }
}