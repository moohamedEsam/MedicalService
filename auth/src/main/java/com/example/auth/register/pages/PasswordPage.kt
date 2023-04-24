package com.example.auth.register.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.common.models.ValidationResult
import com.example.einvoicecomponents.textField.ValidationPasswordTextField
import com.example.common.models.dataType.Password
import com.example.common.models.dataType.PasswordConfirmation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PasswordPage(
    password: Password,
    onPasswordValueChange: (String) -> Unit,
    confirmPassword: PasswordConfirmation,
    modifier: Modifier = Modifier,
    onConfirmPasswordValueChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier
    ) {
        Text("Choose a strong password", style = MaterialTheme.typography.headlineMedium)
        ValidationPasswordTextField(
            value = password.value,
            validation = password.validationResult,
            label = "Password",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onPasswordValueChange,
        )
        ValidationPasswordTextField(
            value = confirmPassword.value,
            validation = confirmPassword.validate(password.value),
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
            password = Password(""),
            onPasswordValueChange = {},
            confirmPassword = PasswordConfirmation(""),
            onConfirmPasswordValueChange = {}
        )
    }
}