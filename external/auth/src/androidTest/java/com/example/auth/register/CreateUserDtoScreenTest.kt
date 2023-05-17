package com.example.auth.register

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.common.models.Result
import com.example.functions.snackbar.FakeSnackBarManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestResult
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CreateUserDtoScreenTest{

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            RegisterScreen(
                onRegistered = {},
                onLoginClick = {},
                onLocationRequested = {},
                lat = 23.0,
                lng = 23.0,
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