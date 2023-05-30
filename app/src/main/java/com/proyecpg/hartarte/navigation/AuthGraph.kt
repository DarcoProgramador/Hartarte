package com.proyecpg.hartarte.navigation

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
            LoginScreen(state = LoginState(isLoading = false), onLoginClick = {
                TODO("Add login event here")
                navController.navigate(Graph.HOME){
                    popUpTo(AuthScreens.LoginScreen.route){
                        inclusive = true
                    }
                }
            })
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

