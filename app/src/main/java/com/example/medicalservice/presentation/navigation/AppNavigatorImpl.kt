package com.example.medicalservice.presentation.navigation

import com.example.common.navigation.AppNavigator
import com.example.common.navigation.NavigationIntent
import kotlinx.coroutines.channels.Channel
import org.koin.core.annotation.Single

@Single([AppNavigator::class])
class AppNavigatorImpl(
    private val navigationChannel: Channel<NavigationIntent>
) : AppNavigator {
    override suspend fun navigateTo(
        destination: String,
        popUpTo: String?,
        singleTop: Boolean,
        inclusive: Boolean
    ) {
        navigationChannel.send(
            NavigationIntent.To(
                destination = destination,
                popUpTo = popUpTo,
                singleTop = singleTop,
                inclusive = inclusive
            )
        )
    }

    override suspend fun navigateBack(vararg arguments: Pair<String, String>) {
        navigationChannel.send(NavigationIntent.Back(arguments.toList()))
    }
}