package com.example.common.models.dataType

import com.example.common.models.ValidationResult
import com.example.common.validators.validatePassword

@JvmInline
value class Password(val value: String){
    val validationResult: ValidationResult
        get() = validatePassword(value)
}
