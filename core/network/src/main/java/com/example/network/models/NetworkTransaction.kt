package com.example.network.models

import com.example.model.app.transaction.Transaction
import com.example.model.app.transaction.TransactionView.Status
import com.example.model.serializers.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class NetworkTransaction(
    val id: String,
    @Serializable(with = DateSerializer::class)
    val createdAt: Date,
    @Serializable(with = DateSerializer::class)
    val updatedAt: Date = Date(),
    val medicineId: String,
    val quantity: Int,
    val receiverId: String,
    val receiverName: String,
    val senderId: String,
    val senderName: String,
    @SerialName("myStatusValue")
    val status: String,
//    @Contextual val donationRequestId: String? = null,
)

fun NetworkTransaction.asDomainModel(): Transaction {
    return Transaction(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        medicineId = medicineId,
        quantity = quantity,
        receiverId = receiverId,
        receiverName = receiverName,
        senderId = senderId,
        senderName = senderName,
        status = Status.valueOf(status),
//        donationRequestId = donationRequestId,
    )
}

fun Transaction.asNetworkModel(): NetworkTransaction {
    return NetworkTransaction(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        medicineId = medicineId,
        quantity = quantity,
        receiverId = receiverId,
        receiverName = receiverName,
        senderId = senderId,
        senderName = senderName,
        status = status.name,
//        donationRequestId = donationRequestId,
    )
}
