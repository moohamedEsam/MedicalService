package com.example.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.domain.RegisterUseCase
import com.example.common.models.SnackBarEvent
import com.example.common.models.ValidationResult
import com.example.common.validators.validateEmail
import com.example.common.validators.validatePassword
import com.example.common.validators.validatePhone
import com.example.common.validators.validateUsername
import com.example.functions.snackbar.SnackBarManager
import com.example.models.auth.Location
import com.example.models.auth.Register
import com.example.models.auth.UserType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val snackBarManager: SnackBarManager
) : ViewModel(), SnackBarManager by snackBarManager {
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    val emailValidationResult = email.map(::validateEmail)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ValidationResult.Empty)

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()
    val passwordValidationResult = password.map(::validatePassword)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ValidationResult.Empty)

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()
    val confirmPasswordValidationResult =
        combine(confirmPassword, password) { confirmPassword, password ->
            if (confirmPassword != password) ValidationResult.Invalid("Passwords do not match")
            else ValidationResult.Valid
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ValidationResult.Empty)

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()
    val usernameValidationResult = username.map(::validateUsername)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ValidationResult.Empty)

    private val _phone = MutableStateFlow("")
    val phone = _phone.asStateFlow()
    val phoneValidationResult = phone.map(::validatePhone)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ValidationResult.Empty)

    private val _type = MutableStateFlow(UserType.Donner)
    val userType = _type.asStateFlow()

    private val _location = MutableStateFlow(Location(0.0, 0.0))
    val location = _location.asStateFlow()

    private val _medicalPrescriptionPath = MutableStateFlow("")
    private val _salaryProofPath = MutableStateFlow("")
    private val _idProofPath = MutableStateFlow("")
    private val receiverValidations = combine(
        _medicalPrescriptionPath,
        userType,
        _salaryProofPath,
        _idProofPath
    ) { medicalPrescriptionPath, userType, salaryProofPath, idProofPath ->
        if (userType != UserType.Receiver) return@combine ValidationResult.Valid
        if (medicalPrescriptionPath.isBlank()) ValidationResult.Invalid("Medical prescription is required")
        else if (salaryProofPath.isBlank()) ValidationResult.Invalid("Salary proof is required")
        else if (idProofPath.isBlank()) ValidationResult.Invalid("ID proof is required")
        else ValidationResult.Valid
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ValidationResult.Empty)

    val isRegisterEnabled = combine(
        emailValidationResult,
        passwordValidationResult,
        usernameValidationResult,
        confirmPasswordValidationResult,
        phoneValidationResult
    ) { validations ->
        validations.all { it is ValidationResult.Valid }
    }.combine(location) { validations, location ->
        validations && location.latitude != 0.0 && location.longitude != 0.0
    }.combine(receiverValidations) { validations, receiverValidations ->
        validations && receiverValidations is ValidationResult.Valid
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    fun setEmail(email: String) {
        _email.update { email }
    }

    fun setPassword(password: String) {
        _password.update { password }
    }

    fun setUsername(username: String) {
        _username.update { username }
    }

    fun setConfirmPassword(confirmPassword: String) {
        _confirmPassword.update { confirmPassword }
    }

    fun setPhone(phone: String) {
        _phone.update { phone }
    }

    fun setUserType(type: UserType) {
        _type.update { type }
    }

    fun setLocation(location: Location) {
        _location.update { location }
    }

    fun setMedicalPrescriptionPath(path: String) {
        _medicalPrescriptionPath.update { path }
    }

    fun setSalaryProofPath(path: String) {
        _salaryProofPath.update { path }
    }

    fun setIdProofPath(path: String) {
        _idProofPath.update { path }
    }


    fun register(onRegisterSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.update { true }
            val result = registerUseCase(getCurrentRegister())
            _isLoading.update { false }
            result.ifFailure {
                val event = SnackBarEvent(
                    message = it ?: "Unknown error",
                    actionLabel = "Retry",
                    action = { register(onRegisterSuccess) }
                )
                showSnackBarEvent(event)
            }
            result.ifSuccess { onRegisterSuccess() }
        }
    }

    private fun getCurrentRegister() = Register(
        username = _username.value,
        email = _email.value,
        password = _password.value,
        phone = _phone.value,
        type = _type.value,
        location = _location.value,
        medicalPrescriptionPath = _medicalPrescriptionPath.value,
        salaryProofPath = _salaryProofPath.value,
        idProofPath = _idProofPath.value,
    )
}