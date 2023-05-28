package com.example.medicalservice.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.settings.ChangeIpUseCase
import com.example.domain.usecase.settings.ObserveIpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingsViewModel(
    private val changeIpUseCase: ChangeIpUseCase,
    private val observeIpUseCase: ObserveIpUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsScreenState())
    val uiState = _uiState.asStateFlow()

    fun handleEvent(event: SettingsScreenEvent) = viewModelScope.launch {
        when (event) {
            SettingsScreenEvent.OnSaveClick -> changeIpUseCase(uiState.value.ip)
            is SettingsScreenEvent.OnIpChange -> _uiState.value = uiState.value.copy(ip = event.ip)
        }
    }

    init {
        viewModelScope.launch {
            observeIpUseCase().distinctUntilChanged().collectLatest {
                _uiState.value = uiState.value.copy(ip = it)
            }
        }
    }
}