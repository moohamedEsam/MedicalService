package com.example.composecomponents.textField

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.common.models.ValidationResult

@Composable
fun ValidationTextFieldContainer(
    modifier: Modifier = Modifier,
    validation: ValidationResult,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier.animateContentSize()) {
        content()

        if (validation is ValidationResult.Invalid)
            Text(
                text = validation.message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                textAlign = TextAlign.End
            )
        else if (validation is ValidationResult.Valid)
            Text(
                text = "Valid ✔",
                color = Color.Green,
                modifier = Modifier.align(Alignment.End),
                maxLines = 1
            )

    }
}

@Preview
@Composable
fun ValidationTextFieldPreview() {
    ValidationTextFieldContainer(validation = ValidationResult.Invalid("Invalid email")) {
        Text(text = "Email")
    }
}