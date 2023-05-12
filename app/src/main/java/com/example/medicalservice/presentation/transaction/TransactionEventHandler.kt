package com.example.medicalservice.presentation.transaction

import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.model.app.transaction.TransactionView
import org.koin.core.annotation.Factory

@Factory
class TransactionEventHandler(
    private val appNavigator: AppNavigator
) {

    suspend fun handleEvent(event: TransactionScreenEvent, transactionView: TransactionView) {
        when (event) {
            TransactionScreenEvent.OnDonationRequestClick -> appNavigator.navigateTo(
                Destination.DonationDetails(transactionView.donationRequestView?.id?:"")
            )

            TransactionScreenEvent.OnMedicineClick -> appNavigator.navigateTo(
                Destination.MedicineDetails(transactionView.medicine.id)
            )
            TransactionScreenEvent.OnReceiverClick -> Unit
            TransactionScreenEvent.OnSenderClick -> Unit
        }
    }


}