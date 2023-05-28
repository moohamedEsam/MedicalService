package com.example.medicalservice.presentation.settings

sealed interface SettingsScreenEvent {
    object OnSaveClick : SettingsScreenEvent
    data class OnIpChange(val ip: String) : SettingsScreenEvent
}