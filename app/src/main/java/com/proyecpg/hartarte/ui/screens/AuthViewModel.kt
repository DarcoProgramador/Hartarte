package com.proyecpg.hartarte.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.proyecpg.hartarte.data.auth.AuthRepository
import com.proyecpg.hartarte.ui.screens.login.LoginEvent
import com.proyecpg.hartarte.ui.screens.login.LoginState
import com.proyecpg.hartarte.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository:AuthRepository,
    val oneTapClient: SignInClient
    ) : ViewModel() {

    var oneTapSignInResponse by mutableStateOf<Resource<BeginSignInResult>>(Resource.Success(null))
        private set
    var signInWithGoogleResponse by mutableStateOf<Resource<FirebaseUser>>(Resource.Success(null))
        private set

    private val _stateLogin = MutableStateFlow(LoginState())
    val stateLogin = _stateLogin.asStateFlow()

    private val _logged = MutableStateFlow(false)
    val logged = _logged.asStateFlow()

    private val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    init {
        viewModelScope.launch {
            isLogged()
        }
    }

    fun process(event : LoginEvent){
        when(event){
            is LoginEvent.LoginClicked -> login(event.email, event.password)
        }
    }

    fun isLogged(){
        if (currentUser != null){
            _logged.update { true }
        }else{
            _logged.update { false }
        }
    }

    fun resetState() {
        _stateLogin.update { LoginState() }
    }

    private fun login(email : String, password: String){
        viewModelScope.launch {
            _stateLogin.update { it.copy(isLoading = true) }
            val result = authRepository.login(email, password)

            result.let {
                when(it){
                    is Resource.Success -> {
                        _stateLogin.update { state ->
                            state.copy(isLoginSuccessful = true,
                                        isLoading = false
                            )
                        }
                        isLogged()
                    }
                    is Resource.Failure -> {
                        _stateLogin.update {state ->
                            state.copy(loginError = "Ha ocurrido un error",
                                        isLoading = false
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _stateLogin.update {state ->
                            state.copy(isLoading = true)
                        }
                    }
                }
            }
        }
    }

    fun signInWithCredentials(credential: AuthCredential){
        viewModelScope.launch {
            oneTapSignInResponse = Resource.Loading

            signInWithGoogleResponse = authRepository.singInWithCredentials(credential)
        }
    }

    fun oneTapSignIn(){
        viewModelScope.launch {
            oneTapSignInResponse = Resource.Loading

            oneTapSignInResponse = authRepository.oneTapSignInWithGoogle()
        }
    }
}