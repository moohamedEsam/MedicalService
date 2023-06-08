package com.example.composecomponents.textField

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.common.models.ValidationResult
import com.example.functions.defaultTextFieldColor

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
            label = { Text(label) },
            keyboardOptions = keyboardOptions,
            colors = defaultTextFieldColor()
        )
    }
}