package com.proyecpg.hartarte.ui.screens.register

import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecpg.hartarte.R
import com.proyecpg.hartarte.data.auth.AuthRepository
import com.proyecpg.hartarte.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun Register(
        user : String,
        email : String,
        password: String,
        comfirmPassword: String
    ){
        viewModelScope.launch {
            _stateRegister.update { it.copy(isLoading = true) }

            if (password != comfirmPassword){
                _stateRegister.update { it.copy(
                    isLoading = false,
                    registerError = R.string.error_password_confirmation_match
                )
                }
                return@launch
            }

            val result = authRepository.signup(user, email, password)

            result.let {
                when(it){
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
                                registerError = R.string.error_Register,
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