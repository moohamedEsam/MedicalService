package com.example.models.app

@kotlinx.serialization.Serializable
data class DiseaseView(
    val id: String,
    val name: String,
    val description: String,
    val symptoms: List<String>,
    val treatment: List<String>,
    val prevention: List<String>,
    val diagnosis: List<String>,
    val medicines: List<Medicine>,
) {
    companion object
}


fun DiseaseView.Companion.empty() = DiseaseView(
    id = "",
    name = "",
    description = "",
    symptoms = emptyList(),
    treatment = emptyList(),
    prevention = emptyList(),
    diagnosis = emptyList(),
    medicines = emptyList(),
)

fun DiseaseView.Companion.headache() = DiseaseView(
    id = "1",
    name = "headache",
    description = "A headache is a pain or discomfort in the head, scalp, or neck. Headaches can be caused by a variety of factors, including stress, lack of sleep, and certain foods. But they can also be a symptom of a more serious condition, such as a brain tumor or a stroke.",
    symptoms = listOf(
        "By definition, a headache is a pain in the head. But the type, location, and severity of pain are highly variable. And for migraine, there can be profound symptoms without a headache.",
        "Headaches can be caused by a variety of factors, including stress, lack of sleep, and certain foods. But they can also be a symptom of a more serious condition, such as a brain tumor or a stroke.",
        "Headaches can be classified in a variety of ways. The most common classification is by the type of pain. The International Headache Society (IHS) has developed a classification system that divides headaches into primary and secondary headaches.",
    ),
    treatment = listOf(
        "Treatment for headaches depends on the type of headache. For example, a person with a tension headache may benefit from over-the-counter pain relievers, while a person with a migraine may need prescription medications.",
        "Some people find that lifestyle changes, such as reducing stress, getting more sleep, and avoiding certain foods, can help prevent headaches.",
        "In some cases, a person may need to see a doctor to determine the cause of their headaches. A doctor may recommend further tests or refer a person to a specialist.",
    ),
    prevention = listOf(
        "Prevention for headaches depends on the type of headache. For example, a person with a tension headache may benefit from over-the-counter pain relievers, while a person with a migraine may need prescription medications.",
        "Some people find that lifestyle changes, such as reducing stress, getting more sleep, and avoiding certain foods, can help prevent headaches.",
        "In some cases, a person may need to see a doctor to determine the cause of their headaches. A doctor may recommend further tests or refer a person to a specialist.",
    ),
    diagnosis = listOf(
        "Diagnosis for headaches depends on the type of headache. For example, a person with a tension headache may benefit from over-the-counter pain relievers, while a person with a migraine may need prescription medications.",
        "Some people find that lifestyle changes, such as reducing stress, getting more sleep, and avoiding certain foods, can help prevent headaches.",
        "In some cases, a person may need to see a doctor to determine the cause of their headaches. A doctor may recommend further tests or refer a person to a specialist.",
    ),
    medicines = listOf(
        Medicine.empty().copy(
            name = "Paracetamol",
            description = "Paracetamol is a painkiller used to treat mild to moderate pain, such as headaches, toothaches, backaches, arthritis, menstrual cramps, colds, and fevers. It is also used to reduce fever in people with cancer."
        ),
        Medicine.empty().copy(
            name = "Ibuprofen",
            description = "Ibuprofen is a nonsteroidal anti-inflammatory drug (NSAID). It works by reducing hormones that cause inflammation and pain in the body. Ibuprofen is used to reduce fever and treat pain or inflammation caused by many conditions such as headache, toothache, back pain, arthritis, menstrual cramps, or minor injury."
        ),
        Medicine.empty().copy(
            name = "Aspirin",
            description = "Aspirin is a pain reliever and a fever reducer. It is used to treat pain or inflammation caused by many conditions such as headache, toothache, back pain, arthritis, menstrual cramps, or minor injury."
        ),
        Medicine.empty().copy(
            name = "Acetaminophen",
            description = "Acetaminophen is a pain reliever and a fever reducer. It is used to treat pain or inflammation caused by many conditions such as headache, toothache, back pain, arthritis, menstrual cramps, or minor injury."
        ),
        Medicine.empty().copy(
            name = "Naproxen",
            description = "Naproxen is a nonsteroidal anti-inflammatory drug (NSAID). It works by reducing hormones that cause inflammation and pain in the body. Naproxen is used to reduce fever and treat pain or inflammation caused by many conditions such as headache, toothache, back pain, arthritis, menstrual cramps, or minor injury."
        ),
    )
)