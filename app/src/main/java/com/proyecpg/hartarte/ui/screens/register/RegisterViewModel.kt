package com.proyecpg.hartarte.ui.screens.register

import android.util.Patterns
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecpg.hartarte.R
import com.proyecpg.hartarte.data.auth.AuthRepository
import com.proyecpg.hartarte.utils.FirebaseErrors
import com.proyecpg.hartarte.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _stateRegister = MutableStateFlow(RegisterState())
    val stateRegister = _stateRegister.asStateFlow()

    private val passwordPattern: Pattern = Pattern.compile(
        "^" +
                "(?=.*[@#$%^&+=*])" +  // Al menos 1 caracter especial
                "(?=\\S+$)" +  // No espacios en blanco
                ".{4,}" +  // Al menos 3 caracteres
                "$"
    )

    fun resetState() {
        _stateRegister.update { RegisterState() }
    }

    fun process(event : RegisterEvent){
        when(event){
            is RegisterEvent.RegisterClicked -> register(event.username, event.email, event.password , event.confirmPassword)
        }
    }

    private fun register(
        username : String,
        email : String,
        password: String,
        confirmPassword: String
    ){
        viewModelScope.launch {
            _stateRegister.update { it.copy(isLoading = true) }

            delay(1000) //Para ver que carga

            val campos = listOf(
                username to R.string.error_missing_username,
                email to R.string.error_missing_email,
                password to R.string.error_missing_password,
                confirmPassword to R.string.error_missing_password_confirmation
            )

            //Valida los campos vacíos
            for ((field, errorMessage) in campos) {
                if (field.isBlank()) {
                    setError( errorMessage.toString() )
                    return@launch
                }
            }

            //Valida el formato del email
            if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
                setError( R.string.error_invalid_email.toString() )
                return@launch
            }

            //Valida si la contraseña es débil
            if (!passwordPattern.matcher(password).matches()){
                setError( R.string.error_password_weak.toString() )
                return@launch
            }

            if (password != confirmPassword){
                setError( R.string.error_password_confirmation_match.toString() )
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

                        val error = FirebaseErrors.handleFirebaseError(resource.exception)

                        if (error != 0){
                            _stateRegister.update { state ->
                                state.copy(
                                    registerError = error.toString(),
                                    isLoading = false
                                )
                            }
                            return@launch
                        }

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

    private fun setError(error: String){
        _stateRegister.update {
            it.copy(
                isLoading = false,
                registerError = error
            )
        }
    }

    fun hideErrorDialog() {
        viewModelScope.launch {
            _stateRegister.update {
                it.copy(
                    registerError = null
                )
            }
        }
    }
}