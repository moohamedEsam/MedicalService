package com.example.medicalservice.presentation.transaction

import com.example.model.app.transaction.TransactionView
import com.example.model.app.transaction.empty
import com.example.model.app.user.User
import com.example.model.app.user.emptyDonor

data class TransactionScreenState(
    val transactionView: TransactionView = TransactionView.empty(),
    val user: User = User.emptyDonor(),
)
