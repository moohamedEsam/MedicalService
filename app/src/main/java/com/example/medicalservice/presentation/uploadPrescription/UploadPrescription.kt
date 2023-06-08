package com.example.medicalservice.presentation.uploadPrescription

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.medicalservice.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun UploadPrescriptionScreen(
    viewModel: UploadPrescriptionViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UploadPrescriptionScreen(
        state = uiState,
        onEvent = viewModel::handleEvent
    )
}

@Composable
fun UploadPrescriptionScreen(
    state: UploadPrescriptionScreenState,
    onEvent: (UploadPrescriptionScreenEvent) -> Unit
) {
    val image = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        onEvent(UploadPrescriptionScreenEvent.OnImagePicked(uri))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(
            onClick = { image.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (state.extractedText.isEmpty() && !state.isLoading) Modifier.weight(1f)
                    else Modifier
                )
        ) {
            Icon(painter = painterResource(id = R.drawable.upload), contentDescription = null)
        }
        if (state.isLoading) CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).scale(2f))
        if (state.extractedText.isEmpty()) return@Column
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Extracted text", style = MaterialTheme.typography.headlineMedium)
                Text(text = state.extractedText, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Button(
            onClick = { onEvent(UploadPrescriptionScreenEvent.OnUploadClicked) },
            modifier = Modifier.align(Alignment.End),
            shape = MaterialTheme.shapes.extraSmall,
            enabled = state.isUploadImageEnabled
        ) {
            Text(text = "Upload")
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun UploadPrescriptionPreview() {
    Surface(modifier=Modifier.fillMaxSize()) {
        UploadPrescriptionScreen(
            state = UploadPrescriptionScreenState(extractedText = "", isLoading = false),
            onEvent = {}
        )
    }

}