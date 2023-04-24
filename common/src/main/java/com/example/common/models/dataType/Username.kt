package com.example.common.models.dataType

import com.example.common.models.ValidationResult
import com.example.common.validators.validateUsername

@JvmInline
value class Username(val value: String){
    val validationResult: ValidationResult
        get() = validateUsername(value)
}
