package com.example.einvoicecomponents.textField

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.common.models.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidationPasswordTextField(
    valueState: StateFlow<String>,
    validationState: StateFlow<ValidationResult>,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val value by valueState.collectAsState()
    val validation by validationState.collectAsState()
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }
    ValidationTextFieldContainer(validation = validation, modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = "$label*") },
            leadingIcon = leadingIcon,
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    if (isPasswordVisible)
                        Icon(
                            imageVector = Icons.Default.VisibilityOff,
                            contentDescription = "Hide password"
                        )
                    else
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "Show password"
                        )
                }
            },
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            maxLines = 1
        )
    }
}

@Preview
@Composable
fun ValidationPasswordTextFieldPreview() {
    ValidationPasswordTextField(
        valueState = MutableStateFlow("123"),
        validationState = MutableStateFlow(ValidationResult.Valid),
        onValueChange = { },
        label = "Password",
    )
}