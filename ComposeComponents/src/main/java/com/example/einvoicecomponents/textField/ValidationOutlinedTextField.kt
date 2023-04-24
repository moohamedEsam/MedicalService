package com.example.einvoicecomponents.textField

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.example.common.models.ValidationResult
import com.example.functions.DefaultTextFieldColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidationOutlinedTextField(
    valueState: StateFlow<String>,
    validationState: StateFlow<ValidationResult>,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    testTag: String = label,
) {
    val value by valueState.collectAsState()
    val validation by validationState.collectAsState()
    ValidationTextFieldContainer(modifier = modifier, validation = validation) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            isError = validation is ValidationResult.Invalid,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag),
            leadingIcon = leadingIcon,
            placeholder = { Text(label) },
            keyboardOptions = keyboardOptions,
            colors = DefaultTextFieldColor()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidationOutlinedTextField(
    value: String,
    validation: ValidationResult,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    testTag: String = label,
) {
    ValidationTextFieldContainer(modifier = modifier, validation = validation) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            isError = validation is ValidationResult.Invalid,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag),
            leadingIcon = leadingIcon,
            placeholder = { Text(label) },
            keyboardOptions = keyboardOptions,
            colors = DefaultTextFieldColor()
        )
    }
}



@Preview
@Composable
fun ValidationOutlinedTextFieldPreview() {
    Surface {
        ValidationOutlinedTextField(
            valueState = MutableStateFlow("mohamed"),
            validationState = MutableStateFlow(ValidationResult.Valid),
            label = "Email",
            onValueChange = {}
        )
    }
}