package com.example.database.models.donation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.database.models.medicine.MedicineEntity
import com.example.database.models.medicine.MedicineEntityView
import com.example.database.models.medicine.toMedicineView
import com.example.database.models.transaction.TransactionEntity
import com.example.database.models.transaction.toTransaction
import com.example.model.app.donation.DonationRequestView

data class DonationRequestEntityView(
    @Embedded val donationRequestEntity: DonationRequestEntity,
    @Relation(
        parentColumn = "medicineId",
        entityColumn = "id",
        entity = MedicineEntity::class,
    )
    val medicine: MedicineEntityView,

    @Relation(
        parentColumn = "id",
        entityColumn = "donationRequestId",
        entity = TransactionEntity::class,
    )
    val transactions:List<TransactionEntity>
)

fun DonationRequestEntityView.toDonationRequestView() = DonationRequestView(
    medicine = medicine.toMedicineView(),
    collected = donationRequestEntity.collected,
    needed = donationRequestEntity.needed,
    contributorsCount = donationRequestEntity.contributorsCount,
    id = donationRequestEntity.id,
    endDate = donationRequestEntity.endDate,
    isBookmarked = donationRequestEntity.isBookmarked,
    transactions = transactions.map { it.toTransaction() }
)