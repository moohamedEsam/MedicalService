package com.example.functions

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable

@Composable
fun defaultTextFieldColor() = OutlinedTextFieldDefaults.colors(
    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
    errorLeadingIconColor = MaterialTheme.colorScheme.error,
    focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
    errorTrailingIconColor = MaterialTheme.colorScheme.error,
    focusedLabelColor = MaterialTheme.colorScheme.primary,
)