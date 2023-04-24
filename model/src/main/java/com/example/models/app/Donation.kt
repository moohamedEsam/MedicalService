package com.example.models.app

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