package com.example.common.functions

import android.content.Context

const val fileName = "prefs"

fun saveToken(androidContext: Context, value: String?) {
    val sharedPref = androidContext.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString(SharedPrefsKey.TOKEN.name, value)
        apply()
    }
}

fun loadToken(context: Context): String? {
    val sharedPref = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    return sharedPref.getString(SharedPrefsKey.TOKEN.name, null)
}

fun saveToSharedPrefs(context: Context, key: SharedPrefsKey, value: String?) {
    val sharedPref = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString(key.name, value)
        apply()
    }
}

fun loadFromSharedPrefs(context: Context, key: SharedPrefsKey): String? {
    val sharedPref = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    return sharedPref.getString(key.name, null)
}

enum class SharedPrefsKey{
    TOKEN,
    EMAIL,
    PASSWORD,
    USER_TYPE,
}