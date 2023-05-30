package com.proyecpg.hartarte.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.proyecpg.hartarte.ui.screens.AuthViewModel
import com.proyecpg.hartarte.ui.screens.home.HomeScreen

@Composable
fun NavigationRoot(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel
) {
    NavHost(
    navController = navController,
    route = Graph.ROOT,
    startDestination = Graph.HOME
    ){
        authNavGraph(
            navController = navController,
            authViewModel = authViewModel
        )
        composable(Graph.HOME){
            HomeScreen(
                viewModel = authViewModel,
                navigateToAuthScrens = {
                    navController.navigate(Graph.AUTHENTICATION){
                        popUpTo(Graph.HOME){
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"
}

sealed class AppScreens(val route: String){
    object MainScreen: AppScreens("main_screen")
    object HomeScreen: AppScreens("home_screen")
    object BookmarksScreen: AppScreens("bookmarks_screen")
    object UserScreen: AppScreens("user_screen")
}