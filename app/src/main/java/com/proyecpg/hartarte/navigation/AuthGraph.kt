package com.proyecpg.hartarte.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.proyecpg.hartarte.ui.screens.login.LoginScreen
import com.proyecpg.hartarte.ui.screens.login.LoginState
import com.proyecpg.hartarte.ui.screens.register.RegisterScreen
import com.proyecpg.hartarte.ui.screens.register.RegisterState

fun NavGraphBuilder.authNavGraph(navController: NavHostController){
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreens.LoginScreen.route
    ){
        composable(AuthScreens.LoginScreen.route){
            LoginScreen(state = LoginState(isLoading = false))
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

