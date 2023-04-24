package com.example.medicalservice.presentation.home.receiver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicalservice.domain.GetCurrentUserUseCase
import com.example.models.app.User
import com.example.models.app.emptyReceiver

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
    private val _user: MutableStateFlow<User> = MutableStateFlow(User.emptyReceiver())
    val user = _user.asStateFlow()

    init{
        viewModelScope.launch(coroutineExceptionHandler) {
            _user.value = getCurrentUserUseCase()
        }
    }
}