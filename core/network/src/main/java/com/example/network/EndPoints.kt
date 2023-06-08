package com.example.network

import android.content.Context
import android.util.Log
import com.example.datastore.dataStore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

object EndPoints {
    private var BASE_URL = "http://192.168.1.2:3000/api"

    fun login() = "$BASE_URL/users/auth/login"
    fun register() = "$BASE_URL/users/adduser"
    fun donationRequest() = "$BASE_URL/donationrequest/getalldonationrequest"
    fun medicine() = "$BASE_URL/medicine/getallmedicine"
    fun disease() = "$BASE_URL/disease/getalldisease"
    fun symptom() = "$BASE_URL/users/model/symptoms"
    fun predictDisease() = "$BASE_URL/predictdisease"
    fun user() = "$BASE_URL/users"

    fun createDisease(): String = "$BASE_URL/disease/adddisease"
    fun getUser(id: String): String = "${user()}/$id"

    fun getUsers(): String = "${user()}/getalluser"

    fun getCurrentUser(): String = "${user()}/getalluser"
    fun getUserTransactions(): String = "$BASE_URL/transaction/gettransactionbyuserid"
    fun getTransactions(): String = "$BASE_URL/transaction/getalltransaction"

    fun createTransaction(): String = "$BASE_URL/transaction/addtransaction"

    fun updateTransaction(id: String): String = "$BASE_URL/transaction/updatetransaction/$id"

    fun getDiagnosisRequests(): String = "$BASE_URL/diagnosesrequest/getalldiagnosesrequest"

    fun createDiagnosisRequest(): String = "$BASE_URL/diagnosesrequest/adddiagnosesrequest"

    fun updateDiagnosisRequest(id: String): String =
        "$BASE_URL/diagnosesrequest/updatediagnosesrequest/$id"

    fun getDiagnosisResults(): String = "$BASE_URL/diagnosesresult/getalldiagnosesresult"

    fun getCurrentUserDiagnosisResult(): String =
        "$BASE_URL/diagnosesresult/getdiagnosesresultforuserid"

    fun createDiagnosisResult(): String = "$BASE_URL/diagnosesresult/adddiagnosesresult"

    fun updateDiagnosisResult(id: String): String =
        "$BASE_URL/diagnosesresult/updatediagnosesresult/$id"

    suspend fun Context.updateBaseUrl() {
        dataStore.data.map { it.remoteServerIp }.distinctUntilChanged().collectLatest {
            Log.i("EndPoints", "updateBaseUrl: $it")
            BASE_URL = "http://$it/api"
        }
    }

    fun createMedicine(): String = "$BASE_URL/medicine/addmedicine"
}