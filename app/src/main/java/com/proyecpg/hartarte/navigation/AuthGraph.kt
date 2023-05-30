package com.proyecpg.hartarte.navigation

import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.proyecpg.hartarte.ui.screens.AuthViewModel
import com.proyecpg.hartarte.ui.screens.login.LoginScreen
import com.proyecpg.hartarte.ui.screens.login.LoginState
import com.proyecpg.hartarte.ui.screens.register.RegisterScreen
import com.proyecpg.hartarte.ui.screens.register.RegisterState

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
                onEventLogin = authViewModel::process
            )
        }
        composable(AuthScreens.RegisterScreen.route){
            RegisterScreen(state = RegisterState(isLoading = false))
        }
    }
}

sealed class AuthScreens(val route: String){
    object LoginScreen: AppScreens("login_screen")
    object RegisterScreen: AppScreens("register_screen")
}

