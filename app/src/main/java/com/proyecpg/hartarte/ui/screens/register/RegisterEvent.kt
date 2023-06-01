package com.proyecpg.hartarte.ui.screens.register

sealed class RegisterEvent {
    class RegisterClicked(val username : String, val email : String, val password : String, val confirmPassword: String) : RegisterEvent()
}