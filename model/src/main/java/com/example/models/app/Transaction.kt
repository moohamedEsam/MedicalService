package com.example.models.app

import com.example.serializers.DateSerializer
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Transaction(
    val id: String,
    @Serializable(with = DateSerializer::class)
    val createdAt: Date,
    @Serializable(with = DateSerializer::class)
    val updatedAt: Date,
    val medicine: Medicine,
    val quantity:Int,
    val receiverId: String,
    val receiverName: String,
    val senderId: String,
    val senderName: String,
    val status: Status,
    val donationRequest: DonationRequest? = null,
) {
    enum class Status {
        Pending, Delivered, Rejected, Completed, Cancelled, InProgress
    }

    companion object
}

fun Transaction.Companion.empty() = Transaction(
    id = "",
    createdAt = Date(),
    updatedAt = Date(),
    medicine = Medicine.empty(),
    quantity = 0,
    receiverId = "",
    receiverName = "",
    senderId = "",
    senderName = "",
    status = Transaction.Status.values().random(),
)
