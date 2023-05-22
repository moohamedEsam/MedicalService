package com.example.medicalservice.presentation.uploadPrescription

import android.net.Uri

sealed interface UploadPrescriptionScreenEvent {
    class OnImagePicked(val uri: Uri) : UploadPrescriptionScreenEvent

    object OnUploadClicked : UploadPrescriptionScreenEvent
}
