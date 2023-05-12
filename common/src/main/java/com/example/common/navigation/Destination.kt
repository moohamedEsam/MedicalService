package com.example.common.navigation

sealed class Destination(val route: String, private vararg val params: String) {

    val fullRoute: String
        get() = buildString {
            append(route)
            params.forEach { param ->
                append("/{$param}")
            }
        }

    sealed class NoArgumentsDestination(route: String) : Destination(route) {
        operator fun invoke() = route
    }

    object Login : NoArgumentsDestination("login")
    class Register : Destination("Register", Map.latKey, Map.lngKey) {
        operator fun invoke(lat: Double = 0.0, lng: Double = 0.0) =
            "$route/$lat/$lng"
    }

    object Map : NoArgumentsDestination("map") {
        const val latKey = "lat"
        const val lngKey = "lng"
    }

    object Home : NoArgumentsDestination("home")

    object TransactionDetails : Destination("transaction", "id") {
        operator fun invoke(id: String = " ") = "$route/$id"
        const val transactionIdKey = "id"
    }

    object DonationsList : NoArgumentsDestination("donations")

    object MyDonationsList : NoArgumentsDestination("my-donations")
    object Search : NoArgumentsDestination("search")

    object DonationDetails : Destination("donation", "id") {
        operator fun invoke(id: String = " ") = "$route/$id"
        const val donationIdKey = "id"
    }

    object DiseaseDetails : Destination("disease", "id") {
        operator fun invoke(id: String = " ") = "$route/$id"
        const val diseaseIdKey = "id"
    }

    object MedicineDetails : Destination("medicine", "id") {
        operator fun invoke(id: String = " ") = "$route/$id"
        const val medicineIdKey = "id"
    }

}