package com.example.common.functions

import android.content.Context

const val fileName = "prefs"
const val tokenKey = "token"


fun saveTokenToSharedPref(androidContext: Context, value: String?) {
    val sharedPref = androidContext.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString(tokenKey, value)
        apply()
    }
}

fun loadTokenFromSharedPref(context: Context): String? {
    val sharedPref = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    return sharedPref.getString(tokenKey, null)
}