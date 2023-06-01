package com.proyecpg.hartarte.navigation

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.proyecpg.hartarte.ui.components.OneTapSignIn
import com.proyecpg.hartarte.ui.components.SignInWithGoogle
import com.proyecpg.hartarte.ui.screens.AuthViewModel
import com.proyecpg.hartarte.ui.screens.login.LoginScreen
import com.proyecpg.hartarte.ui.screens.register.RegisterScreen
import com.proyecpg.hartarte.ui.screens.register.RegisterState
import com.proyecpg.hartarte.ui.screens.register.RegisterViewModel
import com.proyecpg.hartarte.utils.Constants

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
){
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreens.LoginScreen.route
    ){
        composable(AuthScreens.LoginScreen.route){
            val state by authViewModel.stateLogin.collectAsStateWithLifecycle()

            // Lo que hace una vez obtiene el resultado y la data
            val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    try {
                        val credentials = authViewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
                        val googleIdToken = credentials.googleIdToken
                        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
                        authViewModel.signInWithCredentials(googleCredentials)
                    } catch (it: ApiException) {
                        Log.e(Constants.TAG, it.toString())
                    }
                }
            }

            fun launch(signInResult: BeginSignInResult) {
                val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
                launcher.launch(intent)
            }

            OneTapSignIn(launch = {
                launch (it)
            },
            viewModel = authViewModel
            )

            SignInWithGoogle(
                navigateToHomeScreen = { signedIn ->
                    if (signedIn) {
                        navController.navigate(Graph.HOME){
                            popUpTo(AuthScreens.LoginScreen.route){
                                inclusive = true
                            }
                        }
                        authViewModel.resetState()
                    }
                },
                viewModel = authViewModel
            )

            LaunchedEffect(key1 = state.isLoginSuccessful) {
                if (state.isLoginSuccessful){
                    navController.navigate(Graph.HOME){
                        popUpTo(AuthScreens.LoginScreen.route){
                            inclusive = true
                        }
                    }
                    authViewModel.resetState()
                }
            }

            LoginScreen(
                state = state,
                onEventLogin = authViewModel::process,
                onSignInGoogleClick = {
                    authViewModel.oneTapSignIn()
                },
                navigateToRegister = {
                    navController.navigate(AuthScreens.RegisterScreen.route)
                }
            )
        }
        composable(AuthScreens.RegisterScreen.route){
            val registerViewModel : RegisterViewModel = hiltViewModel()
            val stateRegister by registerViewModel.stateRegister.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = stateRegister.isRegisterSuccessful) {
                if (stateRegister.isRegisterSuccessful){
                    navController.popBackStack()
                    registerViewModel.resetState()
                }
            }

            RegisterScreen(
                state = stateRegister,
                navigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterEvent = registerViewModel::process
            )
        }
    }
}

sealed class AuthScreens(val route: String){
    object LoginScreen: AppScreens("login_screen")
    object RegisterScreen: AppScreens("register_screen")
}

