package com.example.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.app.DonationRequest
import java.util.Date

@Entity(tableName = "donationRequests")
data class DonationRequestEntity(
    val medicineId: String,
    val collected: Int,
    val needed: Int,
    val contributorsCount: Int,
    val endDate: Long = Date().time,
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