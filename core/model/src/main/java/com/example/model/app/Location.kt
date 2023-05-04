package com.example.model.app

import com.example.common.models.ValidationResult
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double,
) {
    val validationResult: ValidationResult
        get() = if (latitude == 0.0 || longitude == 0.0)
            ValidationResult.Empty
        else
            ValidationResult.Valid
}
