package com.example.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.einvoicecomponents.OneTimeEventButton
import com.example.einvoicecomponents.textField.ValidationOutlinedTextField
import com.example.einvoicecomponents.textField.ValidationPasswordTextField
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    logo: Any,
    onLoggedIn: () -> Unit,
    onRegisterClick: () -> Unit,
    imageLoader: ImageLoader = get(),
    viewModel: LoginViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LoginScreenContent(
        logo = logo,
        state = uiState,
        onEmailValueChange = { viewModel.handleEvent(LoginScreenEvent.EmailChanged(it)) },
        onPasswordValueChange = { viewModel.handleEvent(LoginScreenEvent.PasswordChanged(it)) },
        onLoginButtonClick = { viewModel.handleEvent(LoginScreenEvent.LoginClicked(onSuccess = onLoggedIn)) },
        onRegisterClick = onRegisterClick,
        imageLoader = imageLoader
    )
}

@Composable
private fun LoginScreenContent(
    logo: Any,
    state: LoginScreenState,
    onEmailValueChange: (String) -> Unit,
    onPasswordValueChange: (String) -> Unit,
    onLoginButtonClick: () -> Unit,
    onRegisterClick: () -> Unit,
    imageLoader: ImageLoader
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = logo,
            modifier = Modifier
                .fillMaxHeight(0.4f)
                .fillMaxWidth(),
            imageLoader = imageLoader,
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Text("Login", style = MaterialTheme.typography.headlineMedium)
            ValidationOutlinedTextField(
                value = state.email,
                validation = state.emailValidationResult,
                label = "Email",
                modifier = Modifier.fillMaxWidth(),
                onValueChange = onEmailValueChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                testTag = "email",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null
                    )
                }
            )

            ValidationPasswordTextField(
                value = state.password,
                validation = state.passwordValidationResult,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = onPasswordValueChange,
                testTag = "password"
            )

            Text(
                text = "Forgot password?",
                modifier = Modifier.align(Alignment.End),
                color = Color.Red
            )
            OneTimeEventButton(
                enabled = state.loginEnabled,
                loading = state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login"),
                onClick = onLoginButtonClick,
                label = "Login"
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(checked = true, onCheckedChange = {})
                Text("Remember me")
            }

            TextButton(onClick = onRegisterClick) {
                Text(
                    buildAnnotatedString {
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                            append("Don't have an account? ")
                        }

                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            append("Register")
                        }
                    }
                )
            }
        }
    }
}


@Preview
@Composable
fun LoginScreenContentPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        LoginScreenContent(
            logo = "",
            state = LoginScreenState(),
            onEmailValueChange = {},
            onPasswordValueChange = {},
            onLoginButtonClick = {},
            onRegisterClick = {},
            imageLoader = ImageLoader.Builder(LocalContext.current).build()
        )
    }
}