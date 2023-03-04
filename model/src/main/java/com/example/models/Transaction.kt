package com.example.models

import java.util.*

data class Transaction(
    val id: String,
    val createdAt: Date,
    val updatedAt: Date,
    val medicine: MedicineView,
    val receiverId: String,
    val receiverName: String,
    val senderId: String,
    val senderName: String,
    val status: Status,
) {
    enum class Status {
        PENDING, ACCEPTED, REJECTED, CANCELED, COMPLETED, IN_PROGRESS
    }
}
