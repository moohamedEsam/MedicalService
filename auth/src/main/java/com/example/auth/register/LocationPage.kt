package com.example.auth.register

import android.location.Geocoder
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.models.auth.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LocationPage(
    locationState: StateFlow<Location>,
    onLocationRequested: () -> Unit,
) {
    val location by locationState.collectAsState()
    Column {
        if (location.latitude != 0.0 && location.longitude != 0.0) {
            val address = Geocoder(LocalContext.current).getFromLocation(
                location.latitude,
                location.longitude,
                1
            )?.get(0)?.locality ?: "${location.longitude}, ${location.latitude}}"
            Text(
                text = "Your location is $address",
                style = MaterialTheme.typography.headlineSmall
            )
        } else
            Text(
                text = "Select your location to finish the registration",
                style = MaterialTheme.typography.headlineSmall
            )
        IconButton(
            onClick = onLocationRequested,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.scale(2f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationPagePreview() {
    LocationPage(
        locationState = MutableStateFlow(Location(0.0, 0.0)),
        onLocationRequested = { }
    )
}