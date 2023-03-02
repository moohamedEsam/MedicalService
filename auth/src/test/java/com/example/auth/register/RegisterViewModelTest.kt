package com.example.auth.register

import app.cash.turbine.test
import com.example.common.models.Result
import com.example.functions.snackbar.FakeSnackBarManager
import com.example.models.auth.Location
import com.example.models.auth.UserType
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After

import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private lateinit var viewModel: RegisterViewModel

    @OptIn(DelicateCoroutinesApi::class)
    private val mainCoroutineDispatcher = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainCoroutineDispatcher)
        viewModel = RegisterViewModel(
            registerUseCase = { Result.Success(Unit) },
            snackBarManager = FakeSnackBarManager()
        )
    }

    @Test
    fun `given user type is donner medical, salary and id proofs should not be required`() =
        runTest {
            viewModel.setUserType(UserType.Donner)
            `fill required fields`()
            viewModel.isRegisterEnabled.test {
                val result = kotlin.run {
                    awaitItem() // initial value
                    awaitItem()
                }
                assertThat(result).isTrue()
            }
        }

    @Test
    fun `given user type is receiver, salary and id proofs should be required`() = runBlocking {
        viewModel.setUserType(UserType.Receiver)
        `fill required fields`()
        val job = launch {
            viewModel.isRegisterEnabled.collectLatest {
                assertThat(it).isFalse()
            }
        }
        delay(1000)
        job.cancel()
    }

    @Test
    fun `user type is receiver register is enabled when salary and id proofs are filled`() =
        runTest {
            viewModel.setUserType(UserType.Receiver)
            `fill required fields`()
            viewModel.setMedicalPrescriptionPath("medicalPrescriptionPath")
            viewModel.setSalaryProofPath("salaryProofPath")
            viewModel.setIdProofPath("nationalIdPath")
            viewModel.isRegisterEnabled.test {
                val result = kotlin.run {
                    awaitItem() // initial value
                    awaitItem()
                }
                assertThat(result).isTrue()
            }
        }


    private fun `fill required fields`() {
        viewModel.setEmail("email@gmail.com")
        viewModel.setPassword("password")
        viewModel.setConfirmPassword("password")
        viewModel.setUsername("username")
        viewModel.setPhone("0123456789")
        viewModel.setLocation(Location(2.0, 2.0))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainCoroutineDispatcher.close()
    }
}