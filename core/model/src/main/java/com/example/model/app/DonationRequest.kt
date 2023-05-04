package com.example.model.app

import java.util.Date
import java.util.UUID
import kotlin.random.Random

data class DonationRequest(
    val medicine: Medicine,
    val collected: Int,
    val needed: Int,
    val contributorsCount: Int,
    val id: String,
    val endDate: Long = Date().time,
) {
    companion object
}

fun DonationRequest.Companion.empty() = DonationRequest(
    medicine = Medicine.empty(),
    collected = 0,
    needed = 0,
    contributorsCount = 0,
    id = UUID.randomUUID().toString()
)

fun dummyDonationRequests() = List(10) {
    val needed = Random.nextInt(1, 1000)
    val collected = Random.nextInt(1, needed)
    DonationRequest.empty().copy(
        needed = needed,
        collected = collected,
        contributorsCount = Random.nextInt(1, 100),
        medicine = Medicine.empty().copy(
            name = "Paracetamol",
            description = "Paracetamol is a painkiller and a fever reducer (antipyretic). It is used to treat many conditions such as headache, muscle aches, arthritis, backache, toothaches, colds, and fevers. It is also used to treat pain and fever after surgery. Paracetamol is in a class of medications called analgesics (pain relievers) and antipyretics (fever reducers). It works by blocking the release of certain chemical messengers that cause pain and fever in the body."
        ),
        endDate = Date().time + Random.nextLong(1000000000)
    )
}
