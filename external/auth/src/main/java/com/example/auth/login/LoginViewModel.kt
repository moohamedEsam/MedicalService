package com.example.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.models.SnackBarEvent
import com.example.common.models.dataType.Email
import com.example.common.models.dataType.Password
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.user.LoginUseCase
import com.example.functions.snackbar.SnackBarManager
import com.example.model.app.auth.Credentials
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val snackBarManager: SnackBarManager,
    private val appNavigator: AppNavigator
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginScreenState())
    val uiState = _uiState.asStateFlow()

    fun handleEvent(event: LoginScreenEvent): Job = viewModelScope.launch {
        when (event) {
            is LoginScreenEvent.EmailChanged -> {
                _uiState.update { it.copy(email = Email(event.value)) }
            }

            is LoginScreenEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = Password(event.value)) }
            }

            is LoginScreenEvent.LoginClicked -> {
                if (!uiState.value.loginEnabled) return@launch
                _uiState.update { it.copy(isLoading = true) }
                val result = login()
                result.ifSuccess {
                    appNavigator.navigateTo(
                        Destination.Home(),
                        popUpTo = Destination.Login.fullRoute,
                        inclusive = true
                    )
                }
                result.ifFailure {
                    val snackBarEvent = SnackBarEvent(it ?: "", "Retry") {
                        handleEvent(event)
                    }
                    snackBarManager.showSnackBarEvent(snackBarEvent)
                }
                _uiState.update { it.copy(isLoading = false) }
            }

            LoginScreenEvent.RegisterClicked -> appNavigator.navigateTo(
                Destination.Register().invoke()
            )

            LoginScreenEvent.OnSettingsClick -> appNavigator.navigateTo(Destination.Settings())
        }
    }

    private suspend fun login() = loginUseCase(
        Credentials(
            uiState.value.email.value,
            uiState.value.password.value
        )
    )

}