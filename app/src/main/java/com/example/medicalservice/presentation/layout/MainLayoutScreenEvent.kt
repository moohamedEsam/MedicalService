package com.example.medicalservice.presentation.layout

import androidx.lifecycle.LifecycleOwner

sealed interface MainLayoutScreenEvent {
    object NavigateToHome : MainLayoutScreenEvent
    object NavigateToSearch : MainLayoutScreenEvent
    object NavigateToMyDonations : MainLayoutScreenEvent
    object NavigateToDonationsList : MainLayoutScreenEvent
    data class SyncClicked(val owner: LifecycleOwner) : MainLayoutScreenEvent
}