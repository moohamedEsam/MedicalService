package com.example.model.app

import com.example.model.app.medicine.MedicineView
import com.example.model.app.medicine.empty
import java.util.Date

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
    id = "",
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
