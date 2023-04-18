package com.example.auth.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.common.models.ValidationResult
import com.example.einvoicecomponents.OneTimeEventButton
import com.example.einvoicecomponents.textField.ValidationOutlinedTextField
import com.example.einvoicecomponents.textField.ValidationPasswordTextField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    LoginScreenContent(
        logo = logo,
        email = viewModel.email,
        emailValidation = viewModel.emailValidationResult,
        onEmailValueChange = viewModel::setEmail,
        password = viewModel.password,
        passwordValidation = viewModel.passwordValidationResult,
        onPasswordValueChange = viewModel::setPassword,
        loginButtonEnable = viewModel.enableLogin,
        loading = viewModel.isLoading,
        onLoginButtonClick = { viewModel.login(onLoggedIn) },
        onRegisterClick = onRegisterClick,
        imageLoader = imageLoader
    )
}

@Composable
private fun LoginScreenContent(
    logo: Any,
    email: StateFlow<String>,
    emailValidation: StateFlow<ValidationResult>,
    onEmailValueChange: (String) -> Unit,
    password: StateFlow<String>,
    passwordValidation: StateFlow<ValidationResult>,
    onPasswordValueChange: (String) -> Unit,
    loginButtonEnable: StateFlow<Boolean>,
    loading: StateFlow<Boolean>,
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
                valueState = email,
                validationState = emailValidation,
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
                valueState = password,
                validationState = passwordValidation,
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
                enabled = loginButtonEnable,
                loading = loading,
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
            email = MutableStateFlow(""),
            emailValidation = MutableStateFlow(ValidationResult.Valid),
            onEmailValueChange = {},
            password = MutableStateFlow(""),
            passwordValidation = MutableStateFlow(ValidationResult.Valid),
            onPasswordValueChange = {},
            loginButtonEnable = MutableStateFlow(true),
            onLoginButtonClick = {},
            onRegisterClick = {},
            imageLoader = ImageLoader(LocalContext.current),
            loading = MutableStateFlow(false)
        )
    }
}