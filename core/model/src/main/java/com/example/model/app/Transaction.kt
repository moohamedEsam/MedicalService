package com.example.model.app

import java.util.Date
import java.util.UUID

data class Transaction(
    val medicineId: String,
    val quantity: Int,
    val receiverId: String,
    val receiverName: String,
    val senderId: String,
    val senderName: String,
    val status: TransactionView.Status = TransactionView.Status.Pending,
    val updatedAt: Date = Date(),
    val createdAt: Date = Date(),
    val id: String = UUID.randomUUID().toString(),
//    @Contextual val donationRequestId: String? = null,
)
