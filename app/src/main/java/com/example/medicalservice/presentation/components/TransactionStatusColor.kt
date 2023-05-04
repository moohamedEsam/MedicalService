package com.example.medicalservice.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.model.app.Transaction

@Composable
fun com.example.model.app.Transaction.Status.color() = when (this) {
    com.example.model.app.Transaction.Status.Pending -> Color.LightGray
    com.example.model.app.Transaction.Status.Delivered -> MaterialTheme.colorScheme.primary
    com.example.model.app.Transaction.Status.Rejected -> Color.Red
    com.example.model.app.Transaction.Status.Completed -> MaterialTheme.colorScheme.primary
    com.example.model.app.Transaction.Status.Cancelled -> Color.Gray
    com.example.model.app.Transaction.Status.InProgress -> MaterialTheme.colorScheme.secondary
}