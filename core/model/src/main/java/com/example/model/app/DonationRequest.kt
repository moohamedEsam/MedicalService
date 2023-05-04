package com.example.model.app

import java.util.Date

data class DonationRequest(
    val medicineId: String,
    val collected: Int,
    val needed: Int,
    val contributorsCount: Int,
    val id: String,
    val endDate: Long = Date().time,
)