package com.example.database.models.transaction

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.app.Transaction
import com.example.model.app.TransactionView
import java.util.Date

@Entity(tableName="transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val createdAt: Date,
    val updatedAt: Date,
    val medicineId: String,
    val quantity: Int,
    val receiverId: String,
    val receiverName: String,
    val senderId: String,
    val senderName: String,
    val status: TransactionView.Status,
    val donationRequestId: String? = null,
)

fun TransactionEntity.toTransaction() = Transaction(
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    quantity = quantity,
    receiverId = receiverId,
    receiverName = receiverName,
    senderId = senderId,
    senderName = senderName,
    status = status,
//    donationRequestId = donationRequestId,
    medicineId = medicineId
)

fun Transaction.toEntity() = TransactionEntity(
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    quantity = quantity,
    receiverId = receiverId,
    receiverName = receiverName,
    senderId = senderId,
    senderName = senderName,
    status = status,
//    donationRequestId = donationRequestId,
    medicineId = medicineId
)