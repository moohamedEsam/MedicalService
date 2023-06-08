package com.example.maplocation

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MapViewModel(
    private val coroutineExceptionHandler: CoroutineExceptionHandler,
    private val appNavigator: AppNavigator,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _address = MutableStateFlow<LatLng?>(null)
    val address = _address.asStateFlow()

    private val _suggestions = MutableStateFlow(emptyList<Address>())
    val suggestions = _suggestions.asStateFlow()

    fun setQuery(value: String) = viewModelScope.launch {
        _query.update { value }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun findByLocationName(geocoder: Geocoder) = viewModelScope.launch(coroutineExceptionHandler) {
        _query.debounce(500)
            .filter { it.isNotEmpty() }
            .mapLatest { geocoder.getFromLocationName(it, 5) ?: emptyList() }
            .collect(_suggestions::emit)
    }

    fun animateCamera(animate: suspend () -> Unit) =
        viewModelScope.launch(coroutineExceptionHandler) { animate() }

    fun setAddress(address: LatLng) {
        _address.update { address }
    }

    fun onLocationPicked(latLng: LatLng) = viewModelScope.launch {
        appNavigator.navigateBack(
            Destination.Map.latKey to latLng.latitude.toString(),
            Destination.Map.lngKey to latLng.longitude.toString()
        )
    }
}