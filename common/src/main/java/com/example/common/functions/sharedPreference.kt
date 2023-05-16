package com.example.common.functions

import android.content.Context

const val fileName = "prefs"
const val TokenKey = "token"
fun Context.saveToken(value: String?) {
    val sharedPref = getSharedPreferences(fileName, Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString(TokenKey, value)
        apply()
    }
}

fun Context.loadToken(): String? {
    val sharedPref = getSharedPreferences(fileName, Context.MODE_PRIVATE)
    return sharedPref.getString(TokenKey, null)
}