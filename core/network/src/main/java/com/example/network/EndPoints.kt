package com.example.network

object EndPoints {
    private const val BASE_URL = "http://192.168.1.2:3000/api"

    const val LOGIN = "$BASE_URL/users/auth/login"
    const val REGISTER = "$BASE_URL/users/adduser"
    const val DONATION_REQUEST = "$BASE_URL/donationrequest/getalldonationrequest"
    const val MEDICINE = "$BASE_URL/medicine/getallmedicine"
    const val DISEASE = "$BASE_URL/disease/getalldisease"
    const val SYMPTOM = "$BASE_URL/users/model/symptoms"
    const val PREDICT_DISEASE = "$BASE_URL/predictdisease"
    const val USER = "$BASE_URL/users"

    fun getUser(id: String): String = "$USER/$id"

    fun getUsers(): String = "$USER/getalluser"

    fun getCurrentUser(): String = "$USER/getalluser"
    fun getUserTransactions(): String = "$BASE_URL/transaction/gettransactionbyuserid"
    fun getTransactions(): String = "$BASE_URL/transaction/getalltransaction"

    fun createTransaction(): String = "$BASE_URL/transaction/addtransaction"

    fun updateTransaction(id:String): String = "$BASE_URL/transaction/updatetransaction/$id"

    fun getDiagnosisRequests(): String = "$BASE_URL/diagnosesrequest/getalldiagnosesrequest"

    fun createDiagnosisRequest(): String = "$BASE_URL/diagnosesrequest/adddiagnosesrequest"

    fun updateDiagnosisRequest(id: String): String = "$BASE_URL/diagnosesrequest/updatediagnosesrequest/$id"

    fun getDiagnosisResults(): String = "$BASE_URL/diagnosesresult/getalldiagnosesresult"

    fun getCurrentUserDiagnosisResult(): String = "$BASE_URL/diagnosesresult/getdiagnosesresultforuserid"

    fun createDiagnosisResult(): String = "$BASE_URL/diagnosesresult/adddiagnosesresult"

    fun updateDiagnosisResult(id: String): String = "$BASE_URL/diagnosesresult/updatediagnosesresult/$id"
}