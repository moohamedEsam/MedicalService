package com.example.medicalservice.presentation.donation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import com.example.common.models.ValidationResult
import com.example.medicalservice.presentation.components.UrgentDonationList
import com.example.model.app.DonationRequestView
import com.example.model.app.dummyDonationRequests
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class DonationScreenState(
    val donationRequestViews: Flow<PagingData<DonationRequestView>> = flowOf(PagingData.empty()),
    val selectedDonationRequest: DonationRequestView? = null,
    val query: String = "",
    val quantity: String = "",
    val isLoading: Boolean = false,
) {
    private val isDonationRequestSelected: Boolean
        get() = selectedDonationRequest != null

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

@Preview(showBackground = true)
@Composable
private fun DonationScreenStatePreview() {
    Surface {
        UrgentDonationList(
            donationRequestViewPagingData = flowOf(
                PagingData.from(
                    dummyDonationRequests()
                )
            ),
            title = "Urgent Donation Requests"
        )
    }

}