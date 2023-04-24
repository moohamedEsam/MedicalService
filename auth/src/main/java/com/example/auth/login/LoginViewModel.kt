package com.example.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.domain.LoginUseCase
import com.example.common.models.SnackBarEvent
import com.example.common.validators.validateEmail
import com.example.common.validators.validatePassword
import com.example.functions.snackbar.SnackBarManager
import com.example.models.auth.Credentials
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val snackBarManager: SnackBarManager
) : ViewModel(), SnackBarManager by snackBarManager {
    private val _uiState = MutableStateFlow(LoginScreenState())
    val uiState = _uiState.asStateFlow()

    fun handleEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.value) }
                _uiState.update { it.copy(emailValidationResult = validateEmail(event.value)) }
            }

            is LoginScreenEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.value) }
                _uiState.update { it.copy(passwordValidationResult = validatePassword(event.value)) }
            }

            is LoginScreenEvent.LoginClicked -> {
                if (!uiState.value.loginEnabled) return
                _uiState.update { it.copy(isLoading = true) }
                login { event.onSuccess() }
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun login(onLoggedIn: () -> Unit): Job = viewModelScope.launch {
        val result = loginUseCase(Credentials(uiState.value.email, uiState.value.password))
        result.ifFailure {
            val event = SnackBarEvent(
                message = it ?: "Unknown error",
                action = { login(onLoggedIn) },
                actionLabel = "Retry"
            )
            showSnackBarEvent(event)
        }
        result.ifSuccess { onLoggedIn() }
    }

}