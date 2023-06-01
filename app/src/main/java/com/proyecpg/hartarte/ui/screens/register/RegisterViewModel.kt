package com.proyecpg.hartarte.ui.screens.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecpg.hartarte.R
import com.proyecpg.hartarte.data.auth.AuthRepository
import com.proyecpg.hartarte.utils.Constants
import com.proyecpg.hartarte.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _stateRegister = MutableStateFlow(RegisterState())
    val stateRegister = _stateRegister.asStateFlow()

    fun resetState() {
        _stateRegister.update { RegisterState() }
    }

    fun process(event : RegisterEvent){
        when(event){
            is RegisterEvent.RegisterClicked -> register(event.username, event.email, event.password , event.comfirmPassword)
        }
    }

    private fun register(
        username : String,
        email : String,
        password: String,
        comfirmPassword: String
    ){
        //TODO:Hacer que use los archivos de string resource para mostrar el texto del error cuando se necesite
        viewModelScope.launch {
            _stateRegister.update { it.copy(isLoading = true) }

            delay(1000) //para ver que carga

            if (password != comfirmPassword){
                _stateRegister.update { it.copy(
                    isLoading = false,
                    registerError = "Las contraseñas no coinciden"
                )
                }
                return@launch
            }

            val result = authRepository.signup(username, email, password)

            result.let {
                when(val resource = it){
                    is Resource.Success -> {
                        _stateRegister.update { state ->
                            state.copy(
                                isRegisterSuccessful = true,
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Failure -> {
                        _stateRegister.update {state ->
                            state.copy(
                                registerError = resource.exception.toString(),
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _stateRegister.update {state ->
                            state.copy(isLoading = true)
                        }
                    }
                }
            }
        }
    }
}