package com.example.medicalservice.presentation.layout

import androidx.lifecycle.LifecycleOwner

sealed interface MainLayoutScreenEvent {
    object OnHomeClick : MainLayoutScreenEvent
    object OnSavedClick : MainLayoutScreenEvent
    object OnDonationsClick : MainLayoutScreenEvent

    object OnDiagnosisClick : MainLayoutScreenEvent

    object OnUploadClick : MainLayoutScreenEvent

    object OnLogoutClick : MainLayoutScreenEvent

    object OnSettingsClick : MainLayoutScreenEvent
    data class SyncClicked(val owner: LifecycleOwner) : MainLayoutScreenEvent
}