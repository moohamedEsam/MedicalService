package com.example.model.app.transaction

import com.example.model.app.medicine.MedicineView
import com.example.model.app.medicine.empty
import com.example.model.app.user.User
import com.example.model.app.user.emptyDonor
import com.example.model.app.user.emptyReceiver
import java.util.Date
import java.util.UUID

data class TransactionView(
    val id: String,
    val createdAt: Date,
    val updatedAt: Date,
    val medicine: MedicineView,
    val quantity: Int,
    val receiver: User?,
    val sender: User?,
    val status: Status,
    val isDelivered: Boolean = false,
    val isReceived: Boolean = false,
) {
    enum class Status {
        Pending, Delivered, Rejected, Completed, Cancelled, InProgress, Active, AttachedToDonationRequest
    }

    companion object
}

fun TransactionView.Companion.empty() = TransactionView(
    id = UUID.randomUUID().toString(),
    createdAt = Date(),
    updatedAt = Date(),
    medicine = MedicineView.empty(),
    quantity = 0,
    status = TransactionView.Status.values().random(),
    sender = User.emptyDonor(),
    receiver = User.emptyReceiver(),
)


fun TransactionView.toTransaction() = Transaction(
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    quantity = quantity,
    receiverId = receiver?.id,
    senderId = sender?.id,
    status = status,
    medicineId = medicine.id,
    isDelivered = isDelivered,
    isReceived = isReceived,
)