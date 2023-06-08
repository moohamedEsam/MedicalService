package com.example.database.models.transaction

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.app.transaction.Transaction
import com.example.model.app.transaction.TransactionView
import java.util.Date

@Entity(tableName="transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val createdAt: Date,
    val updatedAt: Date,
    val medicineId: String,
    val quantity: Int,
    val receiverId: String?,
    val senderId: String?,
    val status: TransactionView.Status,
    val isCreated: Boolean = false,
    val isUpdated: Boolean = false,
    val isDeleted: Boolean = false,
    val isDelivered: Boolean = false,
    val isReceived: Boolean = false,
)

fun TransactionEntity.toTransaction() = Transaction(
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    quantity = quantity,
    receiverId = receiverId,
    senderId = senderId,
    status = status,
    medicineId = medicineId,
    isDelivered = isDelivered,
    isReceived = isReceived,
)

fun Transaction.toEntity() = TransactionEntity(
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    quantity = quantity,
    receiverId = receiverId,
    senderId = senderId,
    status = status,
    medicineId = medicineId,
    isDelivered = isDelivered,
    isReceived = isReceived,
)