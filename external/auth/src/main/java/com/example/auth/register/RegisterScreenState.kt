package com.example.auth.register

import com.example.common.models.ValidationResult
import com.example.common.models.dataType.Email
import com.example.common.models.dataType.Password
import com.example.common.models.dataType.PasswordConfirmation
import com.example.common.models.dataType.Phone
import com.example.common.models.dataType.Username
import com.example.model.app.user.Location
import com.example.model.app.user.UserType

data class RegisterScreenState(
    val email: Email = Email(""),
    val password: Password = Password(""),
    val passwordConfirmation: PasswordConfirmation = PasswordConfirmation(""),
    val username: Username = Username(""),
    val phone: Phone = Phone(""),
    val location: Location = Location(0.0, 0.0),
    val userType: UserType? = null,
    val isLoading: Boolean = false
) {
    val registerEnabled: Boolean
        get() = validationResults().all { it is ValidationResult.Valid } && !isLoading && userType != null

    val progress: Float
        get() {
            val validations = validationResults()
            return validations.count { it is ValidationResult.Valid } / validations.size.toFloat()
        }

    private fun validationResults() = listOf(
        email.validationResult,
        password.validationResult,
        passwordConfirmation.validate(password.value),
        username.validationResult,
        phone.validationResult,
        location.validationResult
    )
}
