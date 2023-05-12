package com.example.medicalservice.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.model.app.transaction.TransactionView

@Composable
fun TransactionView.Status.color() = when (this) {
    TransactionView.Status.Pending -> Color.LightGray
    TransactionView.Status.Delivered -> MaterialTheme.colorScheme.primary
    TransactionView.Status.Rejected -> Color.Red
    TransactionView.Status.Completed -> MaterialTheme.colorScheme.primary
    TransactionView.Status.Cancelled -> Color.Gray
    TransactionView.Status.InProgress -> MaterialTheme.colorScheme.secondary
}