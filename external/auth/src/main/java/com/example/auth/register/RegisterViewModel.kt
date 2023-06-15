package com.example.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.models.SnackBarEvent
import com.example.common.models.dataType.Email
import com.example.common.models.dataType.Password
import com.example.common.models.dataType.PasswordConfirmation
import com.example.common.models.dataType.Phone
import com.example.common.models.dataType.Username
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.user.RegisterUseCase
import com.example.functions.snackbar.SnackBarManager
import com.example.model.app.user.CreateUserDto
import com.example.model.app.user.UserType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val snackBarManager: SnackBarManager,
    private val appNavigator: AppNavigator
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterScreenState())
    val uiState = _uiState.asStateFlow()

    fun handleEvent(event: RegisterScreenEvent) {
        when (event) {
            is RegisterScreenEvent.EmailChanged -> _uiState.update { it.copy(email = Email(event.value)) }
            is RegisterScreenEvent.LocationChanged -> _uiState.update { it.copy(location = event.value) }
            is RegisterScreenEvent.PasswordChanged -> _uiState.update {
                it.copy(password = Password(event.value))
            }

            is RegisterScreenEvent.PasswordConfirmationChanged -> _uiState.update {
                it.copy(passwordConfirmation = PasswordConfirmation(event.value))
            }

            is RegisterScreenEvent.PhoneChanged -> _uiState.update { it.copy(phone = Phone(event.value)) }
            is RegisterScreenEvent.UserTypeChanged -> _uiState.update { it.copy(userType = event.value) }
            is RegisterScreenEvent.UsernameChanged -> _uiState.update {
                it.copy(username = Username(event.value))
            }

            is RegisterScreenEvent.RegisterClicked -> {
                if (!uiState.value.registerEnabled) return
                _uiState.update { it.copy(isLoading = true) }
                register {
                    val snackBarEvent = SnackBarEvent(message = "Registered successfully")
                    snackBarManager.showSnackBarEvent(snackBarEvent)
                    appNavigator.navigateBack()
                }
                _uiState.update { it.copy(isLoading = false) }
            }

            RegisterScreenEvent.LocationClicked -> viewModelScope.launch {
                appNavigator.navigateTo(Destination.Map())
            }

            RegisterScreenEvent.LoginClicked -> viewModelScope.launch { appNavigator.navigateBack() }
        }
    }

    fun register(onRegisterSuccess: suspend () -> Unit) {
        viewModelScope.launch {
            val result = registerUseCase(getCurrentRegister())
            result.ifFailure {
                val event = SnackBarEvent(
                    message = it ?: "Unknown error",
                    actionLabel = "Retry",
                    action = { register(onRegisterSuccess) }
                )
                snackBarManager.showSnackBarEvent(event)
            }
            result.ifSuccess { onRegisterSuccess() }
        }
    }

    private fun getCurrentRegister() = CreateUserDto(
        username = uiState.value.username.value,
        email = uiState.value.email.value,
        password = uiState.value.password.value,
        phone = uiState.value.phone.value,
        location = uiState.value.location,
        type = uiState.value.userType ?: UserType.Donner,
    )
}