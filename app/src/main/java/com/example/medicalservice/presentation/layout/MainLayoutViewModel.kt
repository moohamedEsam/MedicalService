package com.example.medicalservice.presentation.layout

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.example.common.models.SnackBarEvent
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.sync.OneTimeSyncWorkUseCase
import com.example.functions.snackbar.SnackBarManager
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MainLayoutViewModel(
    private val oneTimeSyncWorkUseCase: OneTimeSyncWorkUseCase,
    private val snackBarManager: SnackBarManager,
    private val appNavigator: AppNavigator
) : ViewModel() {

    fun handleEvent(event: MainLayoutScreenEvent) = viewModelScope.launch {
        when (event) {
            MainLayoutScreenEvent.NavigateToDonationsList -> onDonationsListClick()
            MainLayoutScreenEvent.NavigateToHome -> onHomeClick()
            MainLayoutScreenEvent.NavigateToMyDonations -> onMyDonationsListClick()
            MainLayoutScreenEvent.NavigateToSearch -> Unit
            is MainLayoutScreenEvent.SyncClicked -> sync(event.owner)
        }
    }

    private fun sync(owner: LifecycleOwner) {
        oneTimeSyncWorkUseCase.invoke().observe(owner) {
            viewModelScope.launch {
                when (it.state) {
                    WorkInfo.State.ENQUEUED -> snackBarManager.showSnackBarEvent(SnackBarEvent("Sync Enqueued"))
                    WorkInfo.State.RUNNING -> snackBarManager.showSnackBarEvent(SnackBarEvent("Syncing..."))
                    WorkInfo.State.SUCCEEDED -> snackBarManager.showSnackBarEvent(SnackBarEvent("Sync Success"))
                    WorkInfo.State.FAILED -> snackBarManager.showSnackBarEvent(SnackBarEvent("Sync Failed"))
                    else -> Unit
                }
            }
        }
    }

    private fun onHomeClick() = viewModelScope.launch {
        appNavigator.navigateTo(
            Destination.Home.fullRoute,
            singleTop = true,
            popUpTo = Destination.Home.fullRoute
        )
    }

    private fun onDonationsListClick() = viewModelScope.launch {
        appNavigator.navigateTo(
            Destination.DonationsList(),
            singleTop = true,
            popUpTo = Destination.Home.fullRoute
        )
    }

    private fun onMyDonationsListClick() = viewModelScope.launch {
        appNavigator.navigateTo(
            Destination.MyDonationsList(),
            singleTop = true,
            popUpTo = Destination.Home.fullRoute
        )
    }

    fun onSearchClick() = viewModelScope.launch {
        appNavigator.navigateTo(
            Destination.Search(),
            singleTop = true,
            popUpTo = Destination.Home.fullRoute
        )
    }
}