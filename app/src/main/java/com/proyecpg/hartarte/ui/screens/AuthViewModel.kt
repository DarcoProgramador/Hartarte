package com.proyecpg.hartarte.ui.screens

import androidx.lifecycle.ViewModel
import com.proyecpg.hartarte.data.auth.AuthRepository
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepository:AuthRepository)
    : ViewModel() {
        
}