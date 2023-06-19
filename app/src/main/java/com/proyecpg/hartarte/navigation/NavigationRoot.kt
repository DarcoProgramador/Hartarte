package com.proyecpg.hartarte.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.proyecpg.hartarte.data.DataStoreUtil
import com.proyecpg.hartarte.ui.screens.AuthViewModel
import com.proyecpg.hartarte.ui.screens.main.MainScreen
import com.proyecpg.hartarte.ui.screens.main.MainState
import com.proyecpg.hartarte.ui.screens.post.create.CreatePostScreen
import com.proyecpg.hartarte.ui.theme.ThemeViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NavigationRoot(
    navController: NavHostController = rememberNavController(),
    dataStoreUtil: DataStoreUtil,
    authViewModel: AuthViewModel,
    themeViewModel: ThemeViewModel
) {
    NavHost(
    navController = navController,
    route = Graph.ROOT,
    startDestination = Graph.MAIN
    ){
        authNavGraph(
            navController = navController,
            authViewModel = authViewModel
        )

        composable(Graph.MAIN){
            val state by authViewModel.logged.collectAsStateWithLifecycle()
            state.let {
                if (it){
                    MainScreen(
                        state = MainState(
                            false
                        ),
                        viewModel = hiltViewModel(),
                        themeViewModel = themeViewModel,
                        onCreatePost = {
                            navController.navigate(AppScreens.CreatePostScreen.route){

                            }
                        },
                        dataStoreUtil = dataStoreUtil
                    )
                }else{
                    LaunchedEffect(false) {
                        navController.navigate(Graph.AUTHENTICATION){
                            popUpTo(Graph.MAIN){
                                inclusive = true
                            }
                        }
                    }
                }
            }
        }

        composable(AppScreens.CreatePostScreen.route){
            CreatePostScreen(
                onReturn = {
                    navController.popBackStack()
                }
            )
        }

        /*
        navigation(
            startDestination = AppScreens.MainScreen.route,
            route = Graph.MAIN
        ) {
            composable(AppScreens.MainScreen.route) {
                HomeScreen(
                    /*viewModel = authViewModel,
                    themeViewModel = themeViewModel,
                    navigateToAuthScreens = {
                        navController.navigate(Graph.AUTHENTICATION){
                            popUpTo(Graph.MAIN){
                                inclusive = true
                            }
                        }
                    },
                    dataStoreUtil = dataStoreUtil,
                    navController = navController*/
                )
            }
            composable(AppScreens.BookmarkScreen.route) {

            }
            composable(AppScreens.UserScreen.route) {
                UserScreen()
            }
        }*/
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val MAIN = "home_graph"
}

sealed class AppScreens(val route: String){
    object MainScreen: AppScreens("main_screen")
    object HomeScreen: AppScreens("home_screen")
    object BookmarkScreen: AppScreens("bookmarks_screen")
    object UserScreen: AppScreens("user_screen")
    object CreatePostScreen: AppScreens("create_post_screen")
}