package com.example.auth.register

import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.common.models.ValidationResult
import com.example.einvoicecomponents.OneTimeEventButton
import com.example.einvoicecomponents.textField.ValidationPasswordTextField
import com.example.einvoicecomponents.textField.ValidationOutlinedTextField
import com.example.models.auth.Location
import com.example.models.auth.UserType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.compose.viewModel

@Composable
fun RegisterScreen(
    logo: Any,
    onRegistered: () -> Unit,
    onLoginClick: () -> Unit,
    onLocationRequested: () -> Unit,
    lat: Double = 0.0,
    lng: Double = 0.0,
    viewModel: RegisterViewModel = koinViewModel(),
    imageLoader: ImageLoader = get()
) {
    if (lat != 0.0 && lng != 0.0) {
        viewModel.setLocation(Location(lat, lng))
    }

    RegisterScreenContent(
        logo = logo,
        username = viewModel.username,
        usernameValidation = viewModel.usernameValidationResult,
        onUsernameValueChange = viewModel::setUsername,
        email = viewModel.email,
        emailValidation = viewModel.emailValidationResult,
        onEmailValueChange = viewModel::setEmail,
        password = viewModel.password,
        passwordValidation = viewModel.passwordValidationResult,
        onPasswordValueChange = viewModel::setPassword,
        confirmPassword = viewModel.confirmPassword,
        confirmPasswordValidation = viewModel.confirmPasswordValidationResult,
        onConfirmPasswordValueChange = viewModel::setConfirmPassword,
        phone = viewModel.phone,
        phoneValidation = viewModel.phoneValidationResult,
        onPhoneValueChange = viewModel::setPhone,
        userType = viewModel.userType,
        onUserTypeChange = viewModel::setUserType,
        onLocationRequested = onLocationRequested,
        onSetMedicalPrescriptionPath = viewModel::setMedicalPrescriptionPath,
        onSetIdCardPath = viewModel::setIdProofPath,
        onSetSalaryProofPath = viewModel::setSalaryProofPath,
        registerButtonEnable = viewModel.isRegisterEnabled,
        loading = viewModel.isLoading,
        onRegisterButtonClick = { viewModel.register(onRegistered) },
        onLoginClick = onLoginClick,
        location = viewModel.location,
        imageLoader = imageLoader
    )
}

@Composable
private fun RegisterScreenContent(
    logo: Any,
    username: StateFlow<String>,
    usernameValidation: StateFlow<ValidationResult>,
    onUsernameValueChange: (String) -> Unit,
    email: StateFlow<String>,
    emailValidation: StateFlow<ValidationResult>,
    onEmailValueChange: (String) -> Unit,
    password: StateFlow<String>,
    passwordValidation: StateFlow<ValidationResult>,
    onPasswordValueChange: (String) -> Unit,
    confirmPassword: StateFlow<String>,
    confirmPasswordValidation: StateFlow<ValidationResult>,
    onConfirmPasswordValueChange: (String) -> Unit,
    phone: StateFlow<String>,
    phoneValidation: StateFlow<ValidationResult>,
    onPhoneValueChange: (String) -> Unit,
    userType: StateFlow<UserType>,
    onUserTypeChange: (UserType) -> Unit,
    location: StateFlow<Location>,
    onLocationRequested: () -> Unit,
    onSetMedicalPrescriptionPath: (String) -> Unit,
    onSetIdCardPath: (String) -> Unit,
    onSetSalaryProofPath: (String) -> Unit,
    registerButtonEnable: StateFlow<Boolean>,
    loading: StateFlow<Boolean>,
    onRegisterButtonClick: () -> Unit,
    onLoginClick: () -> Unit,
    imageLoader: ImageLoader = get()
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = logo,
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth(),
            imageLoader = imageLoader,
            contentDescription = null,
            contentScale = ContentScale.Fit
        )

        ValidationOutlinedTextField(
            valueState = email,
            validationState = emailValidation,
            label = "Email",
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = onEmailValueChange,
        )

        ValidationPasswordTextField(
            valueState = password,
            validationState = passwordValidation,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onPasswordValueChange
        )

        ValidationPasswordTextField(
            valueState = confirmPassword,
            validationState = confirmPasswordValidation,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onConfirmPasswordValueChange,
            label = "Confirm Password"
        )

        ValidationOutlinedTextField(
            valueState = username,
            validationState = usernameValidation,
            label = "Username",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onUsernameValueChange
        )

        ValidationOutlinedTextField(
            valueState = phone,
            validationState = phoneValidation,
            label = "Phone",
            onValueChange = onPhoneValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        )

        LocationRow(location, onLocationRequested)

        UserTypeRow(userType, onUserTypeChange)
        ReceiverAdditionalInfo(
            userTypeState = userType,
            onSetMedicalPrescriptionPath = onSetMedicalPrescriptionPath,
            onSetSalaryProofPath = onSetSalaryProofPath,
            onSetIdCardPath = onSetIdCardPath
        )
        OneTimeEventButton(
            enabled = registerButtonEnable,
            loading = loading,
            label = "Register",
            onClick = onRegisterButtonClick,
            modifier = Modifier.fillMaxWidth()
        )

        TextButton(onClick = onLoginClick) {
            Text("Already have an account? Login")
        }
    }

}

@Composable
private fun LocationRow(locationStateFlow: StateFlow<Location>, onLocationRequested: () -> Unit) {
    val location by locationStateFlow.collectAsState()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Location", style = MaterialTheme.typography.headlineSmall)
        IconButton(onClick = onLocationRequested) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location"
            )
        }
    }
    if (location.latitude == 0.0 || location.longitude == 0.0) return
    val context = LocalContext.current
    val address =
        Geocoder(context).getFromLocation(location.latitude, location.longitude, 1)?.firstOrNull()
    if (address?.locality != null)
        Text(address.locality)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserTypeRow(
    userTypeState: StateFlow<UserType>,
    onUserTypeClick: (UserType) -> Unit
) {
    val userType by userTypeState.collectAsState()
    Text("User Type", style = MaterialTheme.typography.headlineSmall)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        UserType.values().forEach {
            FilterChip(
                onClick = { onUserTypeClick(it) },
                selected = it == userType,
                label = { Text(it.name) }
            )
        }
    }
}

@Composable
private fun ReceiverAdditionalInfo(
    userTypeState: StateFlow<UserType>,
    onSetMedicalPrescriptionPath: (String) -> Unit = {},
    onSetSalaryProofPath: (String) -> Unit = {},
    onSetIdCardPath: (String) -> Unit = {}
) {
    val userType by userTypeState.collectAsState()
    AnimatedVisibility(visible = userType == UserType.Receiver) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Additional Info", style = MaterialTheme.typography.headlineSmall)
            UploadFileHandler(onSetMedicalPrescriptionPath, "Medical Prescription")
            UploadFileHandler(onSetSalaryProofPath, "Salary Proof")
            UploadFileHandler(onSetIdCardPath, "ID Card")
        }
    }
}

@Composable
private fun UploadFileHandler(onSetFilePath: (String) -> Unit, label: String) {
    val permission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { onSetFilePath(it.toString()) }
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label)
        IconButton(onClick = {
            permission.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }) {
            Icon(
                imageVector = Icons.Default.Upload,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    Surface {
        RegisterScreenContent(
            username = MutableStateFlow("mohamed"),
            usernameValidation = MutableStateFlow(ValidationResult.Empty),
            onUsernameValueChange = {},
            email = MutableStateFlow(""),
            emailValidation = MutableStateFlow(ValidationResult.Empty),
            onEmailValueChange = {},
            password = MutableStateFlow(""),
            passwordValidation = MutableStateFlow(ValidationResult.Empty),
            onPasswordValueChange = {},
            confirmPassword = MutableStateFlow(""),
            confirmPasswordValidation = MutableStateFlow(ValidationResult.Empty),
            onConfirmPasswordValueChange = {},
            phone = MutableStateFlow(""),
            phoneValidation = MutableStateFlow(ValidationResult.Empty),
            onPhoneValueChange = {},
            userType = MutableStateFlow(UserType.Receiver),
            onUserTypeChange = {},
            onLocationRequested = {},
            onSetMedicalPrescriptionPath = {},
            onSetIdCardPath = {},
            onSetSalaryProofPath = {},
            registerButtonEnable = MutableStateFlow(true),
            loading = MutableStateFlow(false),
            onRegisterButtonClick = {},
            imageLoader = LocalContext.current.let { ImageLoader.Builder(it).build() },
            logo = Unit,
            onLoginClick = {},
            location = MutableStateFlow(Location(0.0, 0.0))
        )
    }
}