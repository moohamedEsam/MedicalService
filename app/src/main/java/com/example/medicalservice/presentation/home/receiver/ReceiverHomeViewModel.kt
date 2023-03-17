package com.example.medicalservice.presentation.home.receiver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicalservice.domain.GetCurrentUserUseCase
import com.example.models.User
import com.example.models.emptyReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ReceiverHomeViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private val _user: MutableStateFlow<User> = MutableStateFlow(User.emptyReceiver())
    val user = _user.asStateFlow()

    init{
        viewModelScope.launch {
            _user.value = getCurrentUserUseCase()
        }
    }
}