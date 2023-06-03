package com.proyecpg.hartarte.ui.screens.register

import com.proyecpg.hartarte.data.auth.AuthRepository
import com.proyecpg.hartarte.data.auth.AuthRepositoryImp
import io.mockk.mockk
import org.junit.Assert.*

class RegisterViewModelTest{

    private val repository: AuthRepository = mockk()
    private val viewModel = RegisterViewModel(repository)
}