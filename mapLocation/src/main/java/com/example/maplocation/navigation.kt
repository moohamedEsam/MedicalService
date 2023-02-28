package com.example.maplocation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

const val MapScreenRoute = "map"
const val latKey = "lat"
const val lngKey = "lng"
fun NavGraphBuilder.mapScreen(
    onLocationPicked: (lat: Double, lng: Double) -> Unit
) {
    composable(MapScreenRoute) {
        MapScreen {
            onLocationPicked(it.latitude, it.longitude)
        }
    }
}

fun NavHostController.navigateToMapScreen(){
    navigate(MapScreenRoute)
}