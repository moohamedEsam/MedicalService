package com.example.model.app

import com.example.model.serializers.DateSerializer
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Transaction(
    val id: String,
    @Serializable(with = DateSerializer::class)
    val createdAt: Date,
    @Serializable(with = DateSerializer::class)
    val updatedAt: Date,
    val medicineId: String,
    val quantity: Int,
    val receiverId: String,
    val receiverName: String,
    val senderId: String,
    val senderName: String,
    val status: TransactionView.Status,
//    @Contextual val donationRequestId: String? = null,
)
