package com.example.database.models.donation

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.model.app.donation.DonationRequest
import java.util.Date

@Entity(tableName = "donationRequests", indices = [Index("isBookmarked")])
data class DonationRequestEntity(
    val medicineId: String,
    val collected: Int,
    val needed: Int,
    val contributorsCount: Int,
    val endDate: Long = Date().time,
    val isBookmarked: Boolean = false,
    @PrimaryKey val id: String,
)

fun DonationRequestEntity.toDonationRequest() = DonationRequest(
    id = id,
    medicineId = medicineId,
    collected = collected,
    needed = needed,
    contributorsCount = contributorsCount,
    endDate = endDate,
)

fun DonationRequest.toEntity() = DonationRequestEntity(
    id = id,
    medicineId = medicineId,
    collected = collected,
    needed = needed,
    contributorsCount = contributorsCount,
    endDate = endDate,
)