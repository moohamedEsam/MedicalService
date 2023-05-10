package com.example.medicalservice.presentation.navigation

import com.example.common.navigation.AppNavigator

class FakeAppNavigator : AppNavigator {
    override suspend fun navigateTo(
        destination: String,
        popUpTo: String?,
        singleTop: Boolean,
        inclusive: Boolean
    ) {

    }

    override suspend fun navigateBack(vararg arguments: Pair<String, String>) {

    }
}