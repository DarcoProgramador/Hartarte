package com.proyecpg.hartarte.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.proyecpg.hartarte.data.auth.AuthRepository
import com.proyecpg.hartarte.ui.screens.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository:AuthRepository)
    : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _logged = MutableStateFlow(false)
    val logged = _logged.asStateFlow()

    private val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    init {
        viewModelScope.launch {
            isLogged()
        }
    }

    fun changeLogged(){
        viewModelScope.launch{
            _logged.emit(true)
        }
    }

    private suspend fun isLogged(){
        if (currentUser != null){
            _logged.emit(true)
        }else{
            _logged.emit(false)
        }
    }

    fun resetState() {
        _state.update { LoginState() }
    }

    fun login(){
        TODO("Hacer la funcion para loguear")
    }

    fun register(){
        TODO("Hacer la funcion para registrar")
    }


    fun signInWithCredentials(credential: AuthCredential){
        TODO("Hacer logueo con credenciales")
    }

    fun oneTapSignIn(){
        TODO("Hacer llamado al oneTap de google")
    }
}