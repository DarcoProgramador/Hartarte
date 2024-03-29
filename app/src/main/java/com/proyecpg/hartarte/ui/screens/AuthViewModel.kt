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
import com.proyecpg.hartarte.R
import com.proyecpg.hartarte.data.auth.AuthRepository
import com.proyecpg.hartarte.ui.screens.login.LoginEvent
import com.proyecpg.hartarte.ui.screens.login.LoginState
import com.proyecpg.hartarte.utils.FirebaseErrors
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

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()


    private val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    init {
        viewModelScope.launch {
            isLogged()

            _isLoading.emit(false)
        }
    }

    fun process(event : LoginEvent){
        when(event){
            is LoginEvent.LoginClicked -> login(event.email, event.password)

            is LoginEvent.LogoutClicked -> onLogout()
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
        oneTapSignInResponse = Resource.Success(null)
        signInWithGoogleResponse = Resource.Success(null)

    }

    private fun login(email: String, password: String){
        viewModelScope.launch {
            _stateLogin.update { it.copy(isLoading = true) }
            val result = authRepository.login(email, password)

            listOf(
                email to R.string.error_missing_email,
                password to R.string.error_missing_password
            )

            //Valida los campos vacíos
            if (email.isBlank()){
                setError( R.string.error_missing_email.toString() )
                return@launch
            }
            else if(password.isBlank()){
                setError( R.string.error_missing_password.toString() )
                return@launch
            }

            result.let {
                when(val resource = it){
                    is Resource.Success -> {
                        _stateLogin.update { state ->
                            state.copy(isLoginSuccessful = true,
                                        isLoading = false
                            )
                        }
                        isLogged()
                    }
                    is Resource.Failure -> {

                        val error = FirebaseErrors.handleFirebaseError(resource.exception)

                        if (error != 0){
                            _stateLogin.update { state ->
                                state.copy(
                                    loginError = error.toString(),
                                    isLoading = false
                                )
                            }
                            return@launch
                        }

                        _stateLogin.update {state ->
                            state.copy(loginError = resource.exception.toString(),
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

    private fun onLogout(){
        viewModelScope.launch {
            authRepository.logout()
            isLogged()
        }
    }

    private fun setError(error: String){
        _stateLogin.update {
            it.copy(
                isLoading = false,
                loginError = error
            )
        }
    }

    fun updateLoginSuccessful(){
        viewModelScope.launch {
            _stateLogin.update { state ->
                state.copy(
                    isLoginSuccessful = true,
                    isLoading = false
                )
            }
            isLogged()
        }
    }

    fun hideErrorDialog() {
        viewModelScope.launch {
            _stateLogin.update {
                it.copy(
                    loginError = null
                )
            }
        }
    }

    fun getCurrentUserUID() : String = currentUser?.uid?:""
}