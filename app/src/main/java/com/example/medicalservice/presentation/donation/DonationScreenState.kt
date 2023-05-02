package com.example.medicalservice.presentation.donation

import com.example.common.models.ValidationResult
import com.example.models.app.DonationRequest

data class DonationScreenState(
    val donationRequests: List<DonationRequest> = emptyList(),
    val selectedDonationRequestId: String? = null,
    val query: String = "",
    val quantity: String = "",
    val isLoading: Boolean = false,
) {
    val selectedDonationRequest: DonationRequest?
        get() = donationRequests.find { it.id == selectedDonationRequestId }

    private val isDonationRequestSelected: Boolean
        get() = selectedDonationRequestId != null

    val isDonateButtonEnabled: Boolean
        get() = isDonationRequestSelected && quantityValidationResult is ValidationResult.Valid && !isLoading

    private val quantityRange
        get() = selectedDonationRequest?.run {
            (1..needed - collected)
        } ?: 0..0

    val quantityValidationResult = if (quantity.isEmpty())
            ValidationResult.Empty
        else if (quantity.toInt() !in quantityRange)
            ValidationResult.Invalid("Value must be between ${quantityRange.first} and ${quantityRange.last}")
        else ValidationResult.Valid
}
