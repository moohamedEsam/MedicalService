package com.example.medicalservice.presentation.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination

fun NavGraphBuilder.settingsScreen() {
    composable(Destination.Settings.fullRoute) {
        SettingsScreen()
    }
}