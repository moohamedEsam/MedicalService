package com.example.domain.usecase.diagnosis

import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.text.Text

fun interface ExtractPrescriptionFromImageUseCase : suspend (String) -> Task<Text>