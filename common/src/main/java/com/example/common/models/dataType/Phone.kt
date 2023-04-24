package com.example.common.models.dataType

import com.example.common.models.ValidationResult
import com.example.common.validators.validatePhone

@JvmInline
value class Phone(val value: String){
    val validationResult: ValidationResult
        get() = validatePhone(value)
}
