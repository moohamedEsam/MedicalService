package com.example.medicalservice.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun com.example.model.app.TransactionView.Status.color() = when (this) {
    com.example.model.app.TransactionView.Status.Pending -> Color.LightGray
    com.example.model.app.TransactionView.Status.Delivered -> MaterialTheme.colorScheme.primary
    com.example.model.app.TransactionView.Status.Rejected -> Color.Red
    com.example.model.app.TransactionView.Status.Completed -> MaterialTheme.colorScheme.primary
    com.example.model.app.TransactionView.Status.Cancelled -> Color.Gray
    com.example.model.app.TransactionView.Status.InProgress -> MaterialTheme.colorScheme.secondary
}