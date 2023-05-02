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
import com.example.auth.register.pages.EmailAndUsernamePage
import com.example.auth.register.pages.LocationPage
import com.example.auth.register.pages.PasswordPage
import com.example.auth.register.pages.PhoneAndUserTypePage
import com.example.auth.register.pages.RegisterPages
import com.example.models.Location
import com.example.models.auth.UserType
import com.example.common.models.dataType.Email
import com.example.common.models.dataType.Password
import com.example.common.models.dataType.PasswordConfirmation
import com.example.common.models.dataType.Phone
import com.example.common.models.dataType.Username
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
        viewModel.handleEvent(RegisterScreenEvent.LocationChanged(Location(lat, lng)))
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    RegisterScreenContent(
        logo = logo,
        state = state,
        onUsernameValueChange = { viewModel.handleEvent(RegisterScreenEvent.UsernameChanged(it)) },
        onEmailValueChange = { viewModel.handleEvent(RegisterScreenEvent.EmailChanged(it)) },
        onPasswordValueChange = { viewModel.handleEvent(RegisterScreenEvent.PasswordChanged(it)) },
        onConfirmPasswordValueChange = {
            viewModel.handleEvent(RegisterScreenEvent.PasswordConfirmationChanged(it))
        },
        onPhoneValueChange = { viewModel.handleEvent(RegisterScreenEvent.PhoneChanged(it)) },
        onUserTypeChange = { viewModel.handleEvent(RegisterScreenEvent.UserTypeChanged(it)) },
        onLocationRequested = onLocationRequested,
        imageLoader = imageLoader,
        onRegisterButtonClick = {
            viewModel.handleEvent(RegisterScreenEvent.RegisterClicked(onSuccess = onRegistered))
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RegisterScreenContent(
    logo: Any,
    state: RegisterScreenState,
    onUsernameValueChange: (String) -> Unit,
    onEmailValueChange: (String) -> Unit,
    onPasswordValueChange: (String) -> Unit,
    onConfirmPasswordValueChange: (String) -> Unit,
    onPhoneValueChange: (String) -> Unit,
    onUserTypeChange: (UserType) -> Unit,
    onLocationRequested: () -> Unit,
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
        ProgressIndicator(state.progress)
        LogoImage(logo, imageLoader)
        RegisterPager(
            email = state.email,
            onEmailValueChange = onEmailValueChange,
            username = state.username,
            onUsernameValueChange = onUsernameValueChange,
            password = state.password,
            onPasswordValueChange = onPasswordValueChange,
            confirmPassword = state.passwordConfirmation,
            onConfirmPasswordValueChange = onConfirmPasswordValueChange,
            phone = state.phone,
            onPhoneValueChange = onPhoneValueChange,
            userType = state.userType,
            onUserTypeChange = onUserTypeChange,
            location = state.location,
            onLocationRequested = onLocationRequested,
            pagerState = pagerState,
        )
        RegisterActionRow(
            pageToScroll = pageToScroll,
            pagerState = pagerState,
            onRegisterButtonClick = onRegisterButtonClick,
            onPageChangeClick = { pageToScroll = it },
            registerButtonEnabled = state.registerEnabled
        )
    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
private fun RegisterActionRow(
    pageToScroll: Int,
    pagerState: PagerState,
    registerButtonEnabled: Boolean,
    onPageChangeClick: (Int) -> Unit,
    onRegisterButtonClick: () -> Unit
) {
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
    email: Email,
    onEmailValueChange: (String) -> Unit,
    username: Username,
    onUsernameValueChange: (String) -> Unit,
    password: Password,
    onPasswordValueChange: (String) -> Unit,
    confirmPassword: PasswordConfirmation,
    onConfirmPasswordValueChange: (String) -> Unit,
    phone: Phone,
    onPhoneValueChange: (String) -> Unit,
    userType: UserType,
    onUserTypeChange: (UserType) -> Unit,
    pagerState: PagerState,
    location: Location,
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
                    onEmailValueChange = onEmailValueChange,
                    username = username,
                    onUsernameValueChange = onUsernameValueChange
                )
            }

            RegisterPages.Password -> {
                PasswordPage(
                    password = password,
                    onPasswordValueChange = onPasswordValueChange,
                    confirmPassword = confirmPassword,
                    onConfirmPasswordValueChange = onConfirmPasswordValueChange
                )
            }

            RegisterPages.PhoneAndUserType -> {
                PhoneAndUserTypePage(
                    phone = phone,
                    onPhoneValueChange = onPhoneValueChange,
                    userType = userType,
                    onUserTypeValueChange = onUserTypeChange
                )
            }

            RegisterPages.Location -> {
                LocationPage(location = location, onLocationRequested = onLocationRequested)
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
private fun ProgressIndicator(progress: Float) {
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
            onUsernameValueChange = {},
            onEmailValueChange = {},
            onPasswordValueChange = {},
            onConfirmPasswordValueChange = {},
            onPhoneValueChange = {},
            onUserTypeChange = {},
            onLocationRequested = {},
            imageLoader = ImageLoader(LocalContext.current),
            onRegisterButtonClick = {},
            state = RegisterScreenState()
        )
    }
}