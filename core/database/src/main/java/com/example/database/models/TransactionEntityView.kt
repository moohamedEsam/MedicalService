package com.example.database.models

import com.example.model.app.DonationRequest
import com.example.model.app.TransactionView
import java.util.Date

data class TransactionEntityView(
    val id: String,
    val createdAt: Date,
    val updatedAt: Date,
    val medicineId: String,
    val quantity: Int,
    val receiverId: String,
    val receiverName: String,
    val senderId: String,
    val senderName: String,
    val status: TransactionView.Status,
    val donationRequest: DonationRequest? = null,
)
