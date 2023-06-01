package com.proyecpg.hartarte.ui.screens.register

import androidx.annotation.StringRes

data class RegisterState(
    val isRegisterSuccessful: Boolean = false,
    @StringRes val registerError: Int? = null,
    val isLoading: Boolean = false
)