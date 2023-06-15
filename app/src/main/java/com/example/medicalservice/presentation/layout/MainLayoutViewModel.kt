package com.example.medicalservice.presentation.layout

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.example.common.models.SnackBarEvent
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.sync.OneTimeSyncWorkUseCase
import com.example.domain.usecase.user.GetCurrentUserUseCase
import com.example.domain.usecase.user.IsUserLoggedInUseCase
import com.example.domain.usecase.user.LogOutUseCase
import com.example.functions.snackbar.SnackBarManager
import com.example.model.app.user.User
import com.example.model.app.user.emptyReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MainLayoutViewModel(
    private val oneTimeSyncWorkUseCase: OneTimeSyncWorkUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val snackBarManager: SnackBarManager,
    private val appNavigator: AppNavigator
) : ViewModel() {
    private val _user: MutableStateFlow<User> = MutableStateFlow(User.emptyReceiver())
    val user = _user.asStateFlow()
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
        viewModelScope.launch {
            _isLoggedIn.value = isUserLoggedInUseCase()
            getCurrentUserUseCase().collectLatest {
                _user.value = it
            }
        }


    }

    fun handleEvent(event: MainLayoutScreenEvent) = viewModelScope.launch {
        when (event) {
            MainLayoutScreenEvent.OnDonationsClick -> onDonationsListClick()
            MainLayoutScreenEvent.OnHomeClick -> onHomeClick()
            MainLayoutScreenEvent.OnSavedClick -> onMyDonationsListClick()
            MainLayoutScreenEvent.OnDiagnosisClick -> appNavigator.navigateTo(Destination.DiagnosisForm())
            MainLayoutScreenEvent.OnUploadClick -> appNavigator.navigateTo(Destination.UploadPrescription())
            is MainLayoutScreenEvent.SyncClicked -> sync(event.owner)
            MainLayoutScreenEvent.OnLogoutClick -> {
                logOutUseCase().ifSuccess {
                    appNavigator.navigateTo(Destination.Login())
                }
            }

            MainLayoutScreenEvent.OnSettingsClick -> appNavigator.navigateTo(Destination.Settings())
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
            popUpTo = Destination.DonationsList.fullRoute
        )
    }

    private fun onMyDonationsListClick() = viewModelScope.launch {
        appNavigator.navigateTo(
            Destination.MyDonationsList(),
            singleTop = true,
            popUpTo = Destination.MyDonationsList.fullRoute
        )
    }

}