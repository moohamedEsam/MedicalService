package com.example.network

import com.example.model.app.medicine.Medicine
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test

class KtorRemoteDataSourceTest {

    @Test
    fun `test json parsing`() {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
        val body = """
            [
            {"id":"6457abf52bffc8068b055772",
            "name":"Fioricet",
            "uses":["Fioricet capsules contain a combination of acetaminophen, butalbital, and caffeine. Acetaminophen is a pain reliever and fever reducer. Butalbital is in a group of drugs called barbiturates. It relaxes muscle contractions involved in a tension headache. Caffeine is a central nervous system stimulant. It relaxes muscle contractions in blood vessels to improve blood flow."],
            "sideEffects":["confusion, a seizure"],
            "precautions":["Do not use Fioricet if you have used an MAO inhibitor in the past 14 days. A dangerous drug interaction could occur. MAO inhibitors include isocarboxazid, linezolid, methylene blue injection, phenelzine, and tranylcypromine."],
            "overDos":["The first signs of an acetaminophen overdose include loss of appetite, nausea, vomiting, stomach pain, sweating, and confusion or weakness. Later symptoms may include pain in your upper stomach, dark urine, and yellowing of your skin or the whites of your eyes."],
            "additionalInformation":null,
            "diseasesID":["hello","bye"]}
            ]
        """.trimIndent()

        val response: List<Medicine> = json.decodeFromString(body)
        println(response)
    }
}