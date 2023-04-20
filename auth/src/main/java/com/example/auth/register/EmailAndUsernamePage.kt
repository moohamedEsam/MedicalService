package com.example.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.common.models.ValidationResult
import com.example.einvoicecomponents.textField.ValidationOutlinedTextField
import kotlinx.coroutines.flow.StateFlow

@Composable
fun EmailAndUsernamePage(
    email: StateFlow<String>,
    emailValidation: StateFlow<ValidationResult>,
    onEmailValueChange: (String) -> Unit,
    username: StateFlow<String>,
    modifier: Modifier = Modifier,
    usernameValidation: StateFlow<ValidationResult>,
    onUsernameValueChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier
    ) {
        Text("Create an account", style = MaterialTheme.typography.headlineMedium)
        ValidationOutlinedTextField(
            valueState = email,
            validationState = emailValidation,
            label = "Email",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onEmailValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )
        ValidationOutlinedTextField(
            valueState = username,
            validationState = usernameValidation,
            label = "Username",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onUsernameValueChange
        )
    }
}