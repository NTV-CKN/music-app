package com.infix.musicappv1.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.infix.musicappv1.data.model.user.User
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
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _isLoading: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isLoading: SharedFlow<Boolean> = _isLoading

    private val _userSession: MutableStateFlow<User?> = MutableStateFlow(null)
    val userSession: StateFlow<User?> = _userSession

    fun emitLoading(isLoading: Boolean) {
        viewModelScope.launch { _isLoading.emit(isLoading) }
    }

    fun loadUserSession() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            _userSession.emit(authRepository.getUserSession())
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
                authCredential,
                onCompleted
            )

            _isLoading.emit(false)
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.logout()
        }
    }
}