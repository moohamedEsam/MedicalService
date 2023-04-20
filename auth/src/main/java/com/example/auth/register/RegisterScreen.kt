package com.example.auth.register

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.common.models.ValidationResult
import com.example.models.auth.Location
import com.example.models.auth.UserType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    logo: Any,
    onRegistered: () -> Unit,
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
        location = viewModel.location,
        onLocationRequested = onLocationRequested,
        progress = viewModel.progress,
        registerButtonEnable = viewModel.isRegisterEnabled,
        imageLoader = imageLoader,
        onRegisterButtonClick = { viewModel.register(onRegistered) },
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
    progress: StateFlow<Float>,
    registerButtonEnable: StateFlow<Boolean>,
    imageLoader: ImageLoader,
    onRegisterButtonClick: () -> Unit,
) {
    val pagerState = rememberPagerState()
    var pageToScroll by remember { mutableStateOf(pagerState.currentPage) }
    LaunchedEffect(key1 = pageToScroll) { pagerState.animateScrollToPage(pageToScroll) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        ProgressIndicator(progress)
        LogoImage(logo, imageLoader)
        RegisterPager(
            email = email,
            emailValidation = emailValidation,
            onEmailValueChange = onEmailValueChange,
            username = username,
            usernameValidation = usernameValidation,
            onUsernameValueChange = onUsernameValueChange,
            password = password,
            passwordValidation = passwordValidation,
            onPasswordValueChange = onPasswordValueChange,
            confirmPassword = confirmPassword,
            confirmPasswordValidation = confirmPasswordValidation,
            onConfirmPasswordValueChange = onConfirmPasswordValueChange,
            phone = phone,
            phoneValidation = phoneValidation,
            onPhoneValueChange = onPhoneValueChange,
            userType = userType,
            onUserTypeChange = onUserTypeChange,
            location = location,
            onLocationRequested = onLocationRequested,
            pagerState = pagerState,
        )
        RegisterActionRow(
            pageToScroll = pageToScroll,
            pagerState = pagerState,
            onRegisterButtonClick = onRegisterButtonClick,
            onPageChangeClick = { pageToScroll = it },
            registerButtonEnabledState = registerButtonEnable
        )
    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
private fun RegisterActionRow(
    pageToScroll: Int,
    pagerState: PagerState,
    registerButtonEnabledState: StateFlow<Boolean>,
    onPageChangeClick: (Int) -> Unit,
    onRegisterButtonClick: () -> Unit
) {
    val registerButtonEnabled by registerButtonEnabledState.collectAsStateWithLifecycle()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AssistChip(
            onClick = { onPageChangeClick(pageToScroll - 1) },
            label = { Text("Previous") },
            leadingIcon = { Icon(Icons.Default.ArrowBack, null) },
            enabled = pagerState.currentPage != 0
        )

        if (pagerState.currentPage == RegisterPages.values().size - 1)
            AssistChip(
                onClick = onRegisterButtonClick,
                label = { Text("Register") },
                enabled = registerButtonEnabled
            )
        else
            AssistChip(
                onClick = { onPageChangeClick(pageToScroll + 1) },
                label = { Text("Next") },
                trailingIcon = { Icon(Icons.Default.ArrowForward, null) },
                enabled = pagerState.currentPage != RegisterPages.values().size - 1
            )
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun RegisterPager(
    email: StateFlow<String>,
    emailValidation: StateFlow<ValidationResult>,
    onEmailValueChange: (String) -> Unit,
    username: StateFlow<String>,
    usernameValidation: StateFlow<ValidationResult>,
    onUsernameValueChange: (String) -> Unit,
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
    pagerState: PagerState,
    location: StateFlow<Location>,
    onLocationRequested: () -> Unit,
) {
    HorizontalPager(
        pageCount = RegisterPages.values().size,
        contentPadding = PaddingValues(8.dp),
        pageSpacing = 8.dp,
        userScrollEnabled = false,
        state = pagerState
    ) { pageNumber ->
        when (RegisterPages.values().find { it.ordinal == pageNumber }) {
            RegisterPages.EmailAndUsername -> {
                EmailAndUsernamePage(
                    email = email,
                    emailValidation = emailValidation,
                    onEmailValueChange = onEmailValueChange,
                    username = username,
                    usernameValidation = usernameValidation,
                    onUsernameValueChange = onUsernameValueChange
                )
            }

            RegisterPages.Password -> {
                PasswordPage(
                    password = password,
                    passwordValidation = passwordValidation,
                    onPasswordValueChange = onPasswordValueChange,
                    confirmPassword = confirmPassword,
                    confirmPasswordValidation = confirmPasswordValidation,
                    onConfirmPasswordValueChange = onConfirmPasswordValueChange
                )
            }

            RegisterPages.PhoneAndUserType -> {
                PhoneAndUserTypePage(
                    phone = phone,
                    phoneValidation = phoneValidation,
                    onPhoneValueChange = onPhoneValueChange,
                    userType = userType,
                    onUserTypeValueChange = onUserTypeChange
                )
            }

            RegisterPages.Location -> {
                LocationPage(locationState = location, onLocationRequested = onLocationRequested)
            }

            RegisterPages.Done -> {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "You're all set press register to continue",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }

            null -> Unit
        }
    }
}

@Composable
private fun LogoImage(logo: Any, imageLoader: ImageLoader) {
    AsyncImage(
        model = logo,
        modifier = Modifier
            .fillMaxHeight(0.4f)
            .fillMaxWidth(),
        imageLoader = imageLoader,
        contentDescription = null,
        contentScale = ContentScale.Fit
    )
}


@Composable
private fun ProgressIndicator(progressState: StateFlow<Float>) {
    val progress by progressState.collectAsStateWithLifecycle()
    val transition = updateTransition(progress, label = "progress")
    val currentProgress by transition.animateFloat(label = "progress") { it }
    LinearProgressIndicator(
        progress = currentProgress,
        modifier = Modifier
            .fillMaxWidth()
            .scale(scaleX = 1f, scaleY = 1.5f),
    )

}


@Preview
@Composable
fun RegisterScreenPreview() {
    Surface {
        RegisterScreenContent(
            logo = "",
            username = MutableStateFlow(""),
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
            location = MutableStateFlow(Location(0.0, 0.0)),
            onLocationRequested = {},
            progress = MutableStateFlow(0f),
            registerButtonEnable = MutableStateFlow(true),
            imageLoader = ImageLoader(LocalContext.current),
            onRegisterButtonClick = {},
        )
    }
}