package com.example.common.navigation

interface AppNavigator {
    suspend fun navigateTo(
        destination: String,
        popUpTo: String? = null,
        singleTop: Boolean = false,
        inclusive: Boolean = false
    )

    suspend fun navigateBack(vararg arguments: Pair<String, String> = emptyArray())
}