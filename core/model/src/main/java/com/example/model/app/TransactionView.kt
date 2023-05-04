package com.example.model.app

import com.example.model.serializers.DateSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class TransactionView(
    val id: String,
    @Serializable(with = DateSerializer::class)
    val createdAt: Date,
    @Serializable(with = DateSerializer::class)
    val updatedAt: Date,
    val medicine: Medicine,
    val quantity: Int,
    val receiverId: String,
    val receiverName: String,
    val senderId: String,
    val senderName: String,
    val status: Status,
    @Contextual val donationRequestView: DonationRequestView? = null,
) {
    enum class Status {
        Pending, Delivered, Rejected, Completed, Cancelled, InProgress
    }

    companion object
}

fun TransactionView.Companion.empty() = TransactionView(
    id = "",
    createdAt = Date(),
    updatedAt = Date(),
    medicine = Medicine.empty(),
    quantity = 0,
    receiverId = "",
    receiverName = "",
    senderId = "",
    senderName = "",
    status = TransactionView.Status.values().random(),
)
