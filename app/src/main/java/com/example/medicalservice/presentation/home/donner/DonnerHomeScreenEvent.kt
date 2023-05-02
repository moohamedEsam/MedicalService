package com.example.medicalservice.presentation.home.donner

sealed interface DonnerHomeScreenEvent {
    data class OnQueryChange(val query: String) : DonnerHomeScreenEvent
}
