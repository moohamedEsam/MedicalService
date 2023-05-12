package com.example.medicalservice.presentation.home.navigation

import androidx.navigation.NavGraphBuilder
import com.example.medicalservice.presentation.home.donner.donnerHome
import com.example.medicalservice.presentation.home.receiver.receiverHome
import com.example.model.app.user.UserType



fun NavGraphBuilder.homeScreen(
    userType: UserType,
) {
    if (userType == UserType.Donner)
        donnerHome()
    else
        receiverHome()
}