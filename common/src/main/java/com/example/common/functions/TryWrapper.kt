package com.example.common.functions

import com.example.common.models.Result

inline fun <T> tryWrapper(block: () -> Result<T>) =
    try {
        block()
    } catch (e: Exception) {
        Result.Error(e.localizedMessage)
    }
