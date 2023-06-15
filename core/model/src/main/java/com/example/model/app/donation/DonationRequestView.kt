package com.example.model.app.donation

import com.example.model.app.medicine.MedicineView
import com.example.model.app.medicine.empty
import com.example.model.app.medicine.paracetamol
import com.example.model.app.transaction.Transaction
import java.util.Date
import java.util.UUID
import kotlin.random.Random

data class DonationRequestView(
    val medicine: MedicineView,
    val collected: Int,
    val needed: Int,
    val contributorsCount: Int,
    val id: String,
    val isBookmarked: Boolean = false,
    val endDate: Long = Date().time,
    val transactions: List<Transaction> = emptyList()
) {
    companion object
}

fun DonationRequestView.Companion.empty() = DonationRequestView(
    medicine = MedicineView.empty(),
    collected = 0,
    needed = 0,
    contributorsCount = 0,
    id = UUID.randomUUID().toString()
)

fun dummyDonationRequests() = List(10) {
    val needed = Random.nextInt(1, 1000)
    val collected = Random.nextInt(1, needed)
    DonationRequestView.empty().copy(
        needed = needed,
        collected = collected,
        contributorsCount = Random.nextInt(1, 100),
        medicine = MedicineView.paracetamol(),
        endDate = Date().time + Random.nextLong(1000000000)
    )
}
