package com.example.model.app.donation

import com.example.model.app.medicine.Medicine
import com.example.model.app.medicine.empty
import kotlinx.serialization.Serializable

@Serializable
data class Donation(
    val medicine: Medicine,
    val quantity: Int,
){
    companion object
}

fun Donation.Companion.empty() = Donation(
    medicine = Medicine.empty(),
    quantity = 0,
)