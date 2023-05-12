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
import com.example.model.app.user.Location
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    logo: Any,
    lat: Double = 0.0,
    lng: Double = 0.0,
    viewModel: RegisterViewModel = koinViewModel(),
    imageLoader: ImageLoader = get()
) {
    if (lat != 0.0 && lng != 0.0) {
        viewModel.handleEvent(
            RegisterScreenEvent.LocationChanged(
                Location(
                    lat,
                    lng
                )
            )
        )
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    RegisterScreenContent(
        logo = logo,
        state = state,
        imageLoader = imageLoader,
        onEvent = viewModel::handleEvent
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RegisterScreenContent(
    logo: Any,
    state: RegisterScreenState,
    onEvent: (RegisterScreenEvent) -> Unit,
    imageLoader: ImageLoader,
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
            pagerState = pagerState,
            state = state,
            onEvent = onEvent,
        )

        RegisterActionRow(
            pageToScroll = pageToScroll,
            pagerState = pagerState,
            onRegisterButtonClick = { onEvent(RegisterScreenEvent.RegisterClicked) },
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
    state: RegisterScreenState,
    onEvent: (RegisterScreenEvent) -> Unit,
    pagerState: PagerState,
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
                    email = state.email,
                    onEmailValueChange = { onEvent(RegisterScreenEvent.EmailChanged(it)) },
                    username = state.username,
                    onUsernameValueChange = { onEvent(RegisterScreenEvent.UsernameChanged(it)) }
                )
            }

            RegisterPages.Password -> {
                PasswordPage(
                    password = state.password,
                    onPasswordValueChange = { onEvent(RegisterScreenEvent.PasswordChanged(it)) },
                    confirmPassword = state.passwordConfirmation,
                    onConfirmPasswordValueChange = {
                        onEvent(
                            RegisterScreenEvent.PasswordConfirmationChanged(
                                it
                            )
                        )
                    }
                )
            }

            RegisterPages.PhoneAndUserType -> {
                PhoneAndUserTypePage(
                    phone = state.phone,
                    onPhoneValueChange = { onEvent(RegisterScreenEvent.PhoneChanged(it)) },
                    userType = state.userType,
                    onUserTypeValueChange = { onEvent(RegisterScreenEvent.UserTypeChanged(it)) }
                )
            }

            RegisterPages.Location -> {
                LocationPage(
                    location = state.location,
                    onLocationRequested = { onEvent(RegisterScreenEvent.LocationClicked) })
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
            imageLoader = ImageLoader(LocalContext.current),
            state = RegisterScreenState(),
            onEvent = {}
        )
    }
}