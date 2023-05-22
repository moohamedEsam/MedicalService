package com.example.medicalservice.presentation.uploadPrescription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.models.SnackBarEvent
import com.example.domain.usecase.diagnosis.ExtractPrescriptionFromImageUseCase
import com.example.functions.snackbar.SnackBarManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class UploadPrescriptionViewModel(
    private val extractPrescriptionFromImageUseCase: ExtractPrescriptionFromImageUseCase,
    coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {

    private val _uiState = MutableStateFlow(UploadPrescriptionScreenState())
    val uiState = _uiState.asStateFlow()

    fun handleEvent(event: UploadPrescriptionScreenEvent) = viewModelScope.launch {
        when (event) {
            is UploadPrescriptionScreenEvent.OnImagePicked -> _uiState.value =
                _uiState.value.copy(imageUri = event.uri.toString())

            UploadPrescriptionScreenEvent.OnUploadClicked -> Unit
        }
    }

    init {
        viewModelScope.launch(coroutineExceptionHandler) {
            _uiState.map { it.imageUri }.distinctUntilChanged()
                .filterNot(String::isNullOrEmpty)
                .collectLatest {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                extractPrescriptionFromImageUseCase(it)
                    .addOnCompleteListener{ _uiState.value = _uiState.value.copy(isLoading = false) }
                    .addOnSuccessListener { text ->
                        _uiState.value = _uiState.value.copy(extractedText = text?.text ?: "")
                    }
            }
        }
    }
}