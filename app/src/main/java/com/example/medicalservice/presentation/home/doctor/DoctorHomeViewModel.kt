package com.example.medicalservice.presentation.home.doctor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.diagnosis.GetDiagnosisResultsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DoctorHomeViewModel(
    getDiagnosisResultsUseCase: GetDiagnosisResultsUseCase,
    private val appNavigator: AppNavigator
) : ViewModel() {

    private val pager = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = getDiagnosisResultsUseCase()
    ).flow.cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(DoctorHomeScreenState(diagnosisResults = pager))
    val uiState = _uiState.asStateFlow()

    fun handleEvent(event: DoctorHomeScreenEvent) = viewModelScope.launch {
        when (event) {
            is DoctorHomeScreenEvent.DiagnosisResultClicked -> appNavigator.navigateTo(Destination.DiagnosisResultForm(event.diagnosisResultId))
        }
    }
}