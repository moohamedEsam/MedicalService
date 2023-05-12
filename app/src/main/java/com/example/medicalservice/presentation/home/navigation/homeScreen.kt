package com.example.medicalservice.presentation.home.navigation

import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination
import com.example.datastore.UserSettings
import com.example.datastore.dataStore
import com.example.medicalservice.presentation.home.donner.DonnerHomeScreen
import com.example.medicalservice.presentation.home.receiver.ReceiverHomeScreen
import com.example.model.app.user.UserType


fun NavGraphBuilder.homeScreen() {
    composable(Destination.Home.fullRoute){
        val userSettings by LocalContext.current.dataStore.data.collectAsStateWithLifecycle(initialValue = UserSettings())
        if (userSettings.type == UserType.Donner)
            DonnerHomeScreen()
        else if (userSettings.type == UserType.Receiver)
            ReceiverHomeScreen()
    }
}