package com.example.medicalservice.presentation.transaction

sealed interface TransactionScreenEvent {
    object OnMedicineClick : TransactionScreenEvent
    object OnDonationRequestClick : TransactionScreenEvent
    object OnSenderClick : TransactionScreenEvent
    object OnReceiverClick : TransactionScreenEvent
}