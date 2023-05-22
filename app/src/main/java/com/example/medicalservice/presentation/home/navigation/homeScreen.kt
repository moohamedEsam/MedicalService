package com.example.medicalservice.presentation.home.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination
import com.example.domain.usecase.user.GetCurrentUserUseCase
import com.example.medicalservice.presentation.home.donner.DonnerHomeScreen
import com.example.medicalservice.presentation.home.receiver.ReceiverHomeScreen
import com.example.model.app.user.UserType
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.get


fun NavGraphBuilder.homeScreen() {
    composable(Destination.Home.fullRoute) {
        val currentUser: GetCurrentUserUseCase = get()
        var type: UserType? by remember { mutableStateOf(null) }

        LaunchedEffect(key1 = Unit) {
            currentUser().collectLatest {
                type = it.type
            }
        }

        if (type == UserType.Donner)
            DonnerHomeScreen()
        else if (type == UserType.Receiver)
            ReceiverHomeScreen()
    }
}