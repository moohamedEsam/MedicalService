package com.example.medicalservice.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.models.app.Transaction

@Composable
fun Transaction.Status.color() = when (this) {
    Transaction.Status.Pending -> Color.LightGray
    Transaction.Status.Delivered -> MaterialTheme.colorScheme.primary
    Transaction.Status.Rejected -> Color.Red
    Transaction.Status.Completed -> MaterialTheme.colorScheme.primary
    Transaction.Status.Cancelled -> Color.Gray
    Transaction.Status.InProgress -> MaterialTheme.colorScheme.secondary
}