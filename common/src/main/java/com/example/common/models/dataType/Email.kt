package com.example.common.models.dataType

import com.example.common.models.ValidationResult
import com.example.common.validators.validateEmail


@JvmInline
value class Email(val value: String) {
    val validationResult: ValidationResult
        get() = validateEmail(value)
}
