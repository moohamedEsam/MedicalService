package com.example.medicalservice.presentation.donation

import com.example.common.models.ValidationResult
import com.example.model.app.donation.DonationRequestView
import com.example.model.app.donation.empty

data class DonationScreenState(
    val donationRequest: DonationRequestView = DonationRequestView.empty(),
    val query: String = "",
    val quantity: String = "",
    val isLoading: Boolean = false,
) {
    private val quantityRange =
        1..(donationRequest.needed - donationRequest.collected)

    val progress = try {
        donationRequest.collected.toFloat() / donationRequest.needed.toFloat()
    } catch (e: Exception) {
        0f
    }

    val quantityValidationResult = if (quantity.isEmpty())
        ValidationResult.Empty
    else if (quantity.toInt() !in quantityRange)
        ValidationResult.Invalid("Value must be between ${quantityRange.first} and ${quantityRange.last}")
    else ValidationResult.Valid

    val isDonateButtonEnabled: Boolean =
        quantityValidationResult is ValidationResult.Valid && !isLoading
}