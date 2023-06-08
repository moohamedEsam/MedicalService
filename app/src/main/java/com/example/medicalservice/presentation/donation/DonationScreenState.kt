package com.example.medicalservice.presentation.donation

import androidx.paging.PagingData
import com.example.common.models.ValidationResult
import com.example.model.app.donation.DonationRequestView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class DonationScreenState(
    val donationRequestViews: Flow<PagingData<DonationRequestView>> = flowOf(PagingData.empty()),
    val selectedDonationRequest: DonationRequestView? = null,
    val query: String = "",
    val quantity: String = "",
    val isLoading: Boolean = false,
) {
    private val isDonationRequestSelected: Boolean = selectedDonationRequest != null

    private val quantityRange = selectedDonationRequest?.run {
        (1..needed - collected)
    } ?: 0..0

    val quantityValidationResult = if (quantity.isEmpty())
        ValidationResult.Empty
    else if (quantity.toInt() !in quantityRange)
        ValidationResult.Invalid("Value must be between ${quantityRange.first} and ${quantityRange.last}")
    else ValidationResult.Valid

    val isDonateButtonEnabled: Boolean = isDonationRequestSelected && quantityValidationResult is ValidationResult.Valid && !isLoading
}