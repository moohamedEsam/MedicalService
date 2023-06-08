package com.example.model

import com.example.common.navigation.AppNavigator

class FakeAppNavigator : AppNavigator {
    override suspend fun navigateTo(
        destination: String,
        popUpTo: String?,
        singleTop: Boolean,
        inclusive: Boolean
    ) {
        // no-op
    }

    override suspend fun navigateBack(vararg arguments: Pair<String, String>) {
        // no-op
    }
}