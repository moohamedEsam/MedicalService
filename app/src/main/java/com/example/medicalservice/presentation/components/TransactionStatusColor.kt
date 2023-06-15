package com.example.medicalservice.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.model.app.transaction.TransactionView

@Composable
fun TransactionView.Status.color() = when (this) {
    TransactionView.Status.Pending -> Color.LightGray

    TransactionView.Status.Delivered,
    TransactionView.Status.Completed,
    TransactionView.Status.Active,
    -> MaterialTheme.colorScheme.primary

    TransactionView.Status.Rejected -> Color.Red
    TransactionView.Status.Cancelled -> Color.Gray

    TransactionView.Status.InProgress,
    TransactionView.Status.AttachedToDonationRequest
    -> MaterialTheme.colorScheme.secondary

}