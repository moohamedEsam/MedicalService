package com.example.database.models.transaction

import androidx.room.Embedded
import androidx.room.Relation
import com.example.database.models.medicine.MedicineEntity
import com.example.database.models.medicine.MedicineEntityView
import com.example.database.models.medicine.toMedicineView
import com.example.model.app.transaction.TransactionView

data class TransactionEntityView(
    @Embedded val transactionEntity: TransactionEntity,
    @Relation(
        parentColumn = "medicineId",
        entityColumn = "id",
        entity = MedicineEntity::class,
    )
    val medicineEntityView: MedicineEntityView,
)

fun TransactionEntityView.toTransactionView() = TransactionView(
    medicine = medicineEntityView.toMedicineView(),
    quantity = transactionEntity.quantity,
    id = transactionEntity.id,
    createdAt = transactionEntity.createdAt,
    updatedAt = transactionEntity.updatedAt,
    receiverName = transactionEntity.receiverName,
    receiverId = transactionEntity.receiverId,
    senderName = transactionEntity.senderName,
    senderId = transactionEntity.senderId,
    status = transactionEntity.status,
    donationRequestView = null, // todo return donation request view
)
