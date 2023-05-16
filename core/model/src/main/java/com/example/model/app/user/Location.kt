package com.example.model.app.user

import com.example.common.models.ValidationResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    @SerialName("lat")
    val latitude: Double,
    @SerialName("lng")
    val longitude: Double,
) {
    val validationResult: ValidationResult
        get() = if (latitude == 0.0 || longitude == 0.0)
            ValidationResult.Empty
        else
            ValidationResult.Valid
}
