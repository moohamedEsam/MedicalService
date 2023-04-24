package com.example.auth.register.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.einvoicecomponents.textField.ValidationOutlinedTextField
import com.example.common.models.dataType.Email
import com.example.common.models.dataType.Username

@Composable
fun EmailAndUsernamePage(
    email: Email,
    onEmailValueChange: (String) -> Unit,
    username: Username,
    modifier: Modifier = Modifier,
    onUsernameValueChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier
    ) {
        Text("Create an account", style = MaterialTheme.typography.headlineMedium)
        ValidationOutlinedTextField(
            value = email.value,
            validation = email.validationResult,
            label = "Email",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onEmailValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )
        ValidationOutlinedTextField(
            value = username.value,
            validation = username.validationResult,
            label = "Username",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onUsernameValueChange
        )
    }
}