package com.example.auth.login

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import coil.ImageLoader
import com.example.common.models.Result
import com.example.functions.snackbar.FakeSnackBarManager
import com.example.models.auth.Token
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineExceptionHandler
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

class LoginScreenTest{

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        val loginViewModel = LoginViewModel(
            loginUseCase = { Result.Success(Token("")) },
            snackBarManager = FakeSnackBarManager(),
        )

        composeTestRule.setContent {
            val context = LocalContext.current
            LoginScreen(
                logo = "",
                onLoggedIn = {},
                onRegisterClick = {},
                imageLoader = ImageLoader.Builder(context).build(),
                viewModel = loginViewModel
            )
        }
    }

    @Test
    fun notValidEmail_LoginButton_ShouldBeDisabled() {
        composeTestRule.onNodeWithTag("email").performTextInput("not an email")
        composeTestRule.onNodeWithTag("password").performTextInput("password")
        composeTestRule.onNodeWithText("Login").assertIsNotEnabled()
    }

    @Test
    fun validEmail_LoginButton_ShouldBeEnabled() {
        composeTestRule.onNodeWithTag("email").performTextInput("email@gmail.com")
        composeTestRule.onNodeWithTag("password").performTextInput("password")
        composeTestRule.onNodeWithText("Login").assertIsEnabled()
    }
}