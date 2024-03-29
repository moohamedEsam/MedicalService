package com.example.common.functions

import android.content.Context

const val fileName = "prefs"
const val TokenKey = "token"
const val EmailKey = "email"
fun Context.saveToken(value: String?) {
    val sharedPref = getSharedPreferences(fileName, Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString(TokenKey, value)
        apply()
    }
}

fun Context.saveEmail(value: String?) {
    val sharedPref = getSharedPreferences(fileName, Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString(EmailKey, value)
        apply()
    }
}



fun Context.loadToken(): String? {
    val sharedPref = getSharedPreferences(fileName, Context.MODE_PRIVATE)
    return sharedPref.getString(TokenKey, null)
}

fun Context.loadEmail(): String? {
    val sharedPref = getSharedPreferences(fileName, Context.MODE_PRIVATE)
    return sharedPref.getString(EmailKey, null)
}