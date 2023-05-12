package com.example.medicalservice.presentation.home.receiver

import androidx.paging.PagingData
import com.example.model.app.diagnosis.DiagnosisResultView
import com.example.model.app.transaction.TransactionView
import com.example.model.app.user.User
import com.example.model.app.user.emptyReceiver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class ReceiverHomeScreenState(
    val transactions: Flow<PagingData<TransactionView>> = flowOf(PagingData.empty()),
    val isLoading: Boolean = false,
    val latestDiagnosisResult: DiagnosisResultView? = null,
    val query: String = "",
    val user: User.Receiver = User.emptyReceiver(),
    val isSearchVisible: Boolean = false,
)