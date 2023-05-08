package com.example.medicalservice.presentation.home.receiver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.user.GetCurrentUserUseCase
import com.example.model.app.emptyReceiver

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ReceiverHomeViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {
    private val _user: MutableStateFlow<com.example.model.app.User> = MutableStateFlow(com.example.model.app.User.emptyReceiver())
    val user = _user.asStateFlow()

    init{
        viewModelScope.launch(coroutineExceptionHandler) {
            _user.value = getCurrentUserUseCase()
        }
    }
}