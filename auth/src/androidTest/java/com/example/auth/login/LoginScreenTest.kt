package com.example.auth.login

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performTextInput
import coil.ImageLoader
import com.example.common.models.Result
import com.example.functions.snackbar.FakeSnackBarManager
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

class LoginScreenTest : KoinTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        startKoin {
            modules(
                listOf(
                    module {
                        viewModel {
                            LoginViewModel(
                                loginUseCase = { Result.Success(Unit) },
                                snackBarManager = FakeSnackBarManager()
                            )
                        }
                    }
                )
            )
        }

        composeTestRule.setContent {
            val context = LocalContext.current
            LoginScreen(
                logo = "",
                onLoggedIn = {},
                onRegisterClick = {},
                imageLoader = ImageLoader.Builder(context).build()
            )
        }
    }

    @Test
    fun test() {
        composeTestRule.onNodeWithText("Login").assertIsNotEnabled()
    }

    @After
    fun tearDown() {
        stopKoin()
    }

}