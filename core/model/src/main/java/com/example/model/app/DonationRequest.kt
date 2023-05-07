package com.example.model.app

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class DonationRequest(
    val medicineId: String,
    val collected: Int,
    val needed: Int,
    @SerialName("conteibuterCount")
    val contributorsCount: Int,
    val id: String,
    val endDate: Long = Date().time,
){
    companion object
}

fun DonationRequest.Companion.empty() = DonationRequest(
    medicineId = "",
    collected = 0,
    needed = 0,
    contributorsCount = 0,
    id = "",
    endDate = 0
)