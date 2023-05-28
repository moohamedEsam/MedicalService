package com.example.common.functions

import com.example.common.models.Result

inline fun <T> tryWrapper(block: () -> Result<T>) =
    try {
        block()
    } catch (e: Exception) {
//        Log.e("TryWrapper", "tryWrapper: ${e.message}")
//        Log.e("TryWrapper", "tryWrapper: ${e.stackTrace}")
        Result.Error(e.localizedMessage)
    }
