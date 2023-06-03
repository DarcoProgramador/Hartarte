package com.proyecpg.hartarte.ui.screens.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.proyecpg.hartarte.data.auth.AuthRepository
import com.proyecpg.hartarte.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest{

    @Mock
    private lateinit var repository : AuthRepository

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()


    @Before
    fun setUp() {
        //viewModel = RegisterViewModel(repository)
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun onAfter(){
        Dispatchers.resetMain()
    }

    @Test
    fun `Test cuando los datos pasados estan correctos`() = runTest{

        Mockito.`when`(repository.signup(any<String>(), any<String>(), any<String>())).thenReturn(Resource.Success(null))

        val sut = RegisterViewModel(repository)

        sut.process(RegisterEvent.RegisterClicked("Holabb","hola@hola.com","Holabb*123","Holabb*123"))

        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(RegisterState(isRegisterSuccessful = true, isLoading = false), sut.stateRegister.value)
    }
}