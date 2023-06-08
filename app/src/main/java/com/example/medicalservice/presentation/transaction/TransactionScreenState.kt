package com.example.medicalservice.presentation.transaction

import com.example.model.app.transaction.TransactionView
import com.example.model.app.transaction.empty
import com.example.model.app.user.User
import com.example.model.app.user.emptyDonor

data class TransactionScreenState(
    val transactionView: TransactionView = TransactionView.empty(),
    val user: User = User.emptyDonor(),
    val isUserDialogVisible: Boolean = false,
    val showSender: Boolean = false,
) {
    private val userFinishedTransaction = if(user.id == transactionView.sender?.id) transactionView.isDelivered else transactionView.isReceived

    val isCompleteButtonVisible = !userFinishedTransaction
}
