package com.example.medicalservice.presentation.uploadPrescription

data class UploadPrescriptionScreenState(
    val imageUri: String = "",
    val extractedText: String = "",
    val isLoading : Boolean = false,
){
    val isUploadImageEnabled = imageUri.isNotEmpty() && extractedText.isNotEmpty()
}
