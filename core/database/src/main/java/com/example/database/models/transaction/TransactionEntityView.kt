package com.example.database.models.transaction

import androidx.room.Embedded
import androidx.room.Relation
import com.example.database.models.medicine.MedicineEntity
import com.example.database.models.medicine.MedicineEntityView
import com.example.database.models.medicine.toMedicineView
import com.example.database.models.user.UserEntity
import com.example.database.models.user.toUser
import com.example.model.app.transaction.TransactionView
import com.example.model.app.user.User

data class TransactionEntityView(
    @Embedded val transactionEntity: TransactionEntity,
    @Relation(
        parentColumn = "medicineId",
        entityColumn = "id",
        entity = MedicineEntity::class,
    )
    val medicineEntityView: MedicineEntityView,

    @Relation(
        parentColumn = "senderId",
        entityColumn = "id",
        entity = UserEntity::class,
    )
    val sender: UserEntity?,

    @Relation(
        parentColumn = "receiverId",
        entityColumn = "id",
        entity = UserEntity::class,
    )
    val receiver: UserEntity?
)

fun TransactionEntityView.toTransactionView() = TransactionView(
    medicine = medicineEntityView.toMedicineView(),
    quantity = transactionEntity.quantity,
    id = transactionEntity.id,
    createdAt = transactionEntity.createdAt,
    updatedAt = transactionEntity.updatedAt,
    status = transactionEntity.status,
    receiver = receiver?.toUser(),
    sender = sender?.toUser(),
    isDelivered = transactionEntity.isDelivered,
    isReceived = transactionEntity.isReceived,
)
