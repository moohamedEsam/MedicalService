package com.example.medicalservice.presentation.home.receiver

sealed interface ReceiverHomeScreenEvent {
    object OnCreateDiagnosisRequestClicked : ReceiverHomeScreenEvent
    data class OnTransactionClicked(val transactionId: String) : ReceiverHomeScreenEvent
    data class OnMedicineClicked(val medicineId: String) : ReceiverHomeScreenEvent
    object OnDiagnosisClicked : ReceiverHomeScreenEvent
    object OnSearchIconClicked : ReceiverHomeScreenEvent
    object OnFAQClicked : ReceiverHomeScreenEvent
    object OnFeedbackClicked : ReceiverHomeScreenEvent
    data class OnQueryChanged(val query: String) : ReceiverHomeScreenEvent
}