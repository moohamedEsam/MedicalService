package com.example.database.models.donation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.database.models.medicine.MedicineEntity
import com.example.database.models.medicine.MedicineEntityView
import com.example.database.models.medicine.toMedicineView
import com.example.model.app.donation.DonationRequestView

data class DonationRequestEntityView(
    @Embedded val donationRequestEntity: DonationRequestEntity,
    @Relation(
        parentColumn = "medicineId",
        entityColumn = "id",
        entity = MedicineEntity::class,
    )
    val medicine: MedicineEntityView,
)

fun DonationRequestEntityView.toDonationRequestView() = DonationRequestView(
    medicine = medicine.toMedicineView(),
    collected = donationRequestEntity.collected,
    needed = donationRequestEntity.needed,
    contributorsCount = donationRequestEntity.contributorsCount,
    id = donationRequestEntity.id,
    endDate = donationRequestEntity.endDate,
    isBookmarked = donationRequestEntity.isBookmarked,
)