package com.example.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.common.models.ValidationResult
import com.example.einvoicecomponents.textField.ValidationOutlinedTextField
import com.example.einvoicecomponents.textField.ValidationPasswordTextField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PasswordPage(
    password: StateFlow<String>,
    passwordValidation: StateFlow<ValidationResult>,
    onPasswordValueChange: (String) -> Unit,
    confirmPassword: StateFlow<String>,
    confirmPasswordValidation: StateFlow<ValidationResult>,
    modifier: Modifier = Modifier,
    onConfirmPasswordValueChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier
    ) {
        Text("Choose a strong password", style = MaterialTheme.typography.headlineMedium)
        ValidationPasswordTextField(
            valueState = password,
            validationState = passwordValidation,
            label = "Password",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onPasswordValueChange,
        )
        ValidationPasswordTextField(
            valueState = confirmPassword,
            validationState = confirmPasswordValidation,
            label = "Confirm Password",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onConfirmPasswordValueChange,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PasswordPagePreview() {
    Surface {
        PasswordPage(
            password = MutableStateFlow(""),
            passwordValidation = MutableStateFlow(ValidationResult.Valid),
            onPasswordValueChange = {},
            confirmPassword = MutableStateFlow(""),
            confirmPasswordValidation = MutableStateFlow(ValidationResult.Valid),
            onConfirmPasswordValueChange = {}
        )
    }
}