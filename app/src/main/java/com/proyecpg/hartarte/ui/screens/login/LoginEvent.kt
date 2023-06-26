package com.proyecpg.hartarte.ui.screens.login

sealed class LoginEvent {
    class LoginClicked(val email : String, val password : String) : LoginEvent()

    object LogoutClicked : LoginEvent()

}