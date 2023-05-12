package com.example.network

object EndPoints {
    private const val BASE_URL = "http://192.168.1.3:3000/api"

    const val LOGIN = "$BASE_URL/users/auth/login"
    const val REGISTER = "$BASE_URL/users/adduser"
    const val DONATION_REQUEST = "$BASE_URL/donationrequest/getalldonationrequest"
    const val MEDICINE = "$BASE_URL/medicine/getallmedicine"
    const val DISEASE = "$BASE_URL/disease/getalldisease"
    const val SYMPTOM = "$BASE_URL/symptoms"
    const val PREDICT_DISEASE = "$BASE_URL/predictdisease"
    const val USER = "$BASE_URL/users"

    fun getUser(id: String): String = "$USER/$id"

    fun getCurrentUser(): String = "$USER/getalluser"
    fun getUserTransactions(): String = "$BASE_URL/transaction/gettransactionbyuserid"
}