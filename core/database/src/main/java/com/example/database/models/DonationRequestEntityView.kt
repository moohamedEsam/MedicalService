package com.example.database.models

import com.example.model.app.MedicineView
import java.util.Date

data class DonationRequestEntityView(
    val medicine: MedicineView,
    val collected: Int,
    val needed: Int,
    val contributorsCount: Int,
    val endDate: Long = Date().time,
    val id: String,
)
