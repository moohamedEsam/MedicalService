package com.example.model.app.transaction

import com.example.model.app.donation.DonationRequestView
import com.example.model.app.medicine.MedicineView
import com.example.model.app.medicine.empty
import java.util.Date
import java.util.UUID

data class TransactionView(
    val id: String,
    val createdAt: Date,
    val updatedAt: Date,
    val medicine: MedicineView,
    val quantity: Int,
    val receiverId: String,
    val receiverName: String,
    val senderId: String,
    val senderName: String,
    val status: Status,
    val donationRequestView: DonationRequestView? = null,
) {
    enum class Status {
        Pending, Delivered, Rejected, Completed, Cancelled, InProgress
    }

    companion object
}

fun TransactionView.Companion.empty() = TransactionView(
    id = UUID.randomUUID().toString(),
    createdAt = Date(),
    updatedAt = Date(),
    medicine = MedicineView.empty(),
    quantity = 0,
    receiverId = "",
    receiverName = "",
    senderId = "",
    senderName = "",
    status = TransactionView.Status.values().random(),
)
