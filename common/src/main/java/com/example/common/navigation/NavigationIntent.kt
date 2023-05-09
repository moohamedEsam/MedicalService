package com.example.common.navigation

sealed interface NavigationIntent {
    data class To(
        val destination: String,
        val popUpTo: String? = null,
        val singleTop: Boolean = false,
        val inclusive: Boolean = false
    ) : NavigationIntent

    data class Back(val arguments: Collection<Pair<String, String>>) : NavigationIntent
}
