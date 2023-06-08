package com.example.medicalservice.presentation.transaction

sealed interface TransactionScreenEvent {
    object OnMedicineClick : TransactionScreenEvent
    object OnSenderClick : TransactionScreenEvent
    object OnReceiverClick : TransactionScreenEvent

    object OnMarkAsDeliveredClick : TransactionScreenEvent

    object OnUserDialogDismiss : TransactionScreenEvent

    data class OnCallClick(val phoneNumber: String) : TransactionScreenEvent

    data class OnLocationClick(val latitude: Double, val longitude: Double) : TransactionScreenEvent
}