package com.example.auth.register.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.auth.register.RegisterScreenEvent
import com.example.auth.register.RegisterScreenState

@Composable
fun DonePage(
    state: RegisterScreenState,
    onEvent: (RegisterScreenEvent) -> Unit
) {
    Column {
        Text(
            "Terms and conditions",
            style = MaterialTheme.typography.headlineSmall
        )
        if (state.showTerms)
            TermsDialog(onEvent)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = state.agreedToTerms,
                onCheckedChange = { onEvent(RegisterScreenEvent.TermsAgreed(it)) }
            )

            Text(
                buildAnnotatedString {
                    append("I agree to the ")
                    pushStyle(
                        SpanStyle(
                            textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    append("terms and conditions")
                    pop()
                },
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.clickable {
                    onEvent(RegisterScreenEvent.ToggleTermsDialog)
                }
            )
        }

    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TermsDialog(onEvent: (RegisterScreenEvent) -> Unit) {
    AlertDialog(onDismissRequest = {
        onEvent(RegisterScreenEvent.ToggleTermsDialog)
    }) {
        Card {
            Text(
                text = "The application is not responsible for checking the medicine expire date and the user should check it before using the medicine.\n the diagnosis is not accurate and the user double check with a doctor before using the medicine.\n the application is not responsible for any side effect",
                modifier = Modifier.padding(32.dp)
            )
        }
    }
}