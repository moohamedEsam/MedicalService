package com.example.auth.register.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.auth.register.RegisterScreenEvent
import com.example.auth.register.RegisterScreenState
import com.example.composecomponents.textField.ValidationOutlinedTextField

@Composable
fun EmailAndUsernamePage(
    state: RegisterScreenState,
    onEvent: (RegisterScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier
    ) {
        ValidationOutlinedTextField(
            value = state.email.value,
            validation = state.email.validationResult,
            label = "Email",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { onEvent(RegisterScreenEvent.EmailChanged(it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )
        ValidationOutlinedTextField(
            value = state.username.value,
            validation = state.username.validationResult,
            label = "Username",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { onEvent(RegisterScreenEvent.UsernameChanged(it)) },
        )
        ValidationOutlinedTextField(
            value = state.phone.value,
            validation = state.phone.validationResult,
            label = "Phone",
            onValueChange = { onEvent(RegisterScreenEvent.PhoneChanged(it)) },
        )
    }
}