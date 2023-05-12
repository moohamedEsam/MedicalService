package com.example.model.app.disease

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Symptom(val name: String) {
    companion object
}


fun Symptom.Companion.dummyList() = listOf(
    Symptom("itching"),
    Symptom("skin_rash"),
    Symptom("chills"),
    Symptom("joint_pain"),
    Symptom("vomiting"),
    Symptom("fatigue"),
    Symptom("weight_loss"),
    Symptom("lethargy"),
    Symptom("cough"),
    Symptom("high_fever"),
    Symptom("breathlessness"),
    Symptom("sweating"),
    Symptom("headache"),
    Symptom("yellowish_skin"),
    Symptom("dark_urine"),
    Symptom("nausea"),
    Symptom("loss_of_appetite"),
    Symptom("abdominal_pain"),
    Symptom("diarrhoea"),
    Symptom("mild_fever"),
    Symptom("yellowing_of_eyes"),
    Symptom("swelled_lymph_nodes"),
    Symptom("malaise"),
    Symptom("blurred_and_distorted_vision"),
    Symptom("phlegm"),
    Symptom("chest_pain"),
    Symptom("dizziness"),
    Symptom("excessive_hunger"),
    Symptom("loss_of_balance"),
    Symptom("irritability"),
    Symptom("muscle_pain")
)