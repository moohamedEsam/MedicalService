package com.example.models

data class DonationRequest(
    val medicine: Medicine,
    val collected: Int,
    val needed: Int,
    val contributorsCount: Int,
){
    companion object
}

fun DonationRequest.Companion.empty() = DonationRequest(
    medicine = Medicine.empty(),
    collected = 0,
    needed = 0,
    contributorsCount = 0,
)
