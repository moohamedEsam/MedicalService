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
    object Settings : NoArgumentsDestination("settings")
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

    object DiagnosisForm : NoArgumentsDestination("diagnosis form")
    object DiagnosisDetails : Destination("diagnosis", "id") {
        operator fun invoke(id: String) = "$route/$id"
        const val diagnosisIdKey = "id"
    }

    object DiagnosisList : NoArgumentsDestination("diagnosis list")

    object DonationsList : NoArgumentsDestination("donations")

    object MyDonationsList : NoArgumentsDestination("saved donations")

    object UploadPrescription : NoArgumentsDestination("upload-prescription")


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