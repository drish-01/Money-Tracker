package com.drish.moneytracker.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drish.moneytracker.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = MutableStateFlow(authRepository.isLoggedIn).also { flow ->
        viewModelScope.launch {
            authRepository.authStateFlow.collect { user ->
                (flow as MutableStateFlow).value = user != null
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            val result = authRepository.signIn(email, password)
            _uiState.value = if (result.isSuccess) {
                AuthUiState(isSuccess = true)
            } else {
                AuthUiState(errorMessage = result.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }

    fun signUp(email: String, password: String, displayName: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            val result = authRepository.signUp(email, password, displayName)
            _uiState.value = if (result.isSuccess) {
                AuthUiState(isSuccess = true)
            } else {
                AuthUiState(errorMessage = result.exceptionOrNull()?.message ?: "Signup failed")
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            val result = authRepository.resetPassword(email)
            _uiState.value = if (result.isSuccess) {
                AuthUiState(isSuccess = true)
            } else {
                AuthUiState(errorMessage = result.exceptionOrNull()?.message ?: "Reset failed")
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
