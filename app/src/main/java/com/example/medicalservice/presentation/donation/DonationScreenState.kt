package com.example.medicalservice.presentation.donation

import com.example.common.models.ValidationResult

data class DonationScreenState(
    val donationRequestViews: List<com.example.model.app.DonationRequestView> = emptyList(),
    val selectedDonationRequestId: String? = null,
    val query: String = "",
    val quantity: String = "",
    val isLoading: Boolean = false,
) {
    val selectedDonationRequestView: com.example.model.app.DonationRequestView?
        get() = donationRequestViews.find { it.id == selectedDonationRequestId }

    private val isDonationRequestSelected: Boolean
        get() = selectedDonationRequestId != null

    val isDonateButtonEnabled: Boolean
        get() = isDonationRequestSelected && quantityValidationResult is ValidationResult.Valid && !isLoading

    private val quantityRange
        get() = selectedDonationRequestView?.run {
            (1..needed - collected)
        } ?: 0..0

    val quantityValidationResult = if (quantity.isEmpty())
            ValidationResult.Empty
        else if (quantity.toInt() !in quantityRange)
            ValidationResult.Invalid("Value must be between ${quantityRange.first} and ${quantityRange.last}")
        else ValidationResult.Valid
}
