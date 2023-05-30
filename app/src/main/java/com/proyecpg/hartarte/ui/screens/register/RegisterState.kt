package com.proyecpg.hartarte.ui.screens.register

data class RegisterState(
    val isRegisterSuccessful: Boolean = false,
    val registerError: String? = null,
    val isLoading: Boolean = false
)