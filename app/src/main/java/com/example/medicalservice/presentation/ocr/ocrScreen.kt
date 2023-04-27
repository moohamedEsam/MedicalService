package com.example.medicalservice.presentation.ocr

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@Composable
fun OcrScreen() {
    val textRecognizer by remember {
        mutableStateOf(TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS))
    }
    var text by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val image = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        val image = InputImage.fromFilePath(context, uri)
        textRecognizer.process(image).addOnSuccessListener { visualText ->
            text = visualText.text
        }.addOnFailureListener {
            Log.i("ocrScreen", "OcrScreen: ${it.message}")
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Button(onClick = { image.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
            Text(text = "Pick Image")
        }
        Text(text = text, modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .weight(1f))
    }

}