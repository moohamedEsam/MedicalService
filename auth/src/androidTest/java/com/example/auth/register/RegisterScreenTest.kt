package com.example.auth.register

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import coil.ImageLoader
import com.example.auth.domain.RegisterUseCase
import com.example.common.models.Result
import com.example.functions.snackbar.FakeSnackBarManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestResult
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

class RegisterScreenTest{

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            RegisterScreen(
                logo = "",
                onLoginClick = {},
                onRegistered = {},
                onLocationRequested = {},
                lat = 23.0,
                lng = 23.0,
                imageLoader = ImageLoader(ApplicationProvider.getApplicationContext()),
                viewModel = RegisterViewModel(
                    registerUseCase = {
                        delay(2000)
                        Result.Success(Unit)
                    },
                    snackBarManager = FakeSnackBarManager()
                )
            )
        }
    }

    @Test
    fun registerButtonOnlyExistsWhenIsLoadingIsFalse(): TestResult = runBlocking {
        composeTestRule.onNodeWithTag("Email").performTextInput("mohamed@gmail.com")
        composeTestRule.onNodeWithTag("Password").performTextInput("mohamed@gmail.com")
        composeTestRule.onNodeWithTag("Confirm Password").performTextInput("mohamed@gmail.com")
        composeTestRule.onNodeWithTag("Username").performTextInput("mohamed")
        composeTestRule.onNodeWithTag("Phone").performTextInput("0123456789")
        composeTestRule.onNodeWithText("Register").assertIsEnabled()
        composeTestRule.onNodeWithText("Register").performClick()
        delay(100)
        composeTestRule.onNodeWithText("Register").assertIsNotDisplayed()
    }
}