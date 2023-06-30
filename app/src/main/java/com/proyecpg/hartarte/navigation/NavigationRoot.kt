package com.proyecpg.hartarte.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.proyecpg.hartarte.ui.screens.AuthViewModel
import com.proyecpg.hartarte.ui.screens.main.MainScreen
import com.proyecpg.hartarte.ui.screens.main.MainViewModel
import com.proyecpg.hartarte.ui.screens.post.create.CreatePostScreen
import com.proyecpg.hartarte.ui.screens.post.open.OpenPostScreen
import com.proyecpg.hartarte.ui.screens.search.SearchScreen

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NavigationRoot(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel
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
                        onCreatePost = {
                            navController.navigate(AppScreens.CreatePostScreen.route){
                            }
                        },
                        viewModel = mainViewModel,
                        onLogoutClick = authViewModel::process,
                        onSearchClick = {
                            navController.navigate(AppScreens.SearchScreen.route){

                            }
                        },
                        onPostClick = {
                            navController.navigate(AppScreens.OpenPostScreen.route){
                            }
                        }
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

        composable(AppScreens.SearchScreen.route){
            SearchScreen(
                onReturn = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppScreens.CreatePostScreen.route){
            CreatePostScreen(
                onReturn = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppScreens.OpenPostScreen.route){
            OpenPostScreen(
                postId = "01",
                postImages = listOf
                    (
                    "https://cdn.discordapp.com/attachments/1109581677199634522/1109581830883127406/576294.png",
                    "https://cdn.discordapp.com/attachments/1109581677199634522/1109581862520766484/576296.png",
                    "https://cdn.discordapp.com/attachments/1109581677199634522/1109581879872585859/576295.png"
                ),
                postUsername = "HartarteUser",
                postUserPic = "https://cdn.discordapp.com/attachments/1029844385237569616/1116569644745097320/393368.png",
                postTitle = "Título de ejemplo",
                postDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras eget volutpat dui. Pellentesque sollicitudin malesuada augue, in sollicitudin nisi efficitur ut. Sed pellentesque egestas nisi, sed rutrum metus iaculis ultricies. Vivamus libero nunc, elementum eget massa faucibus, pretium mattis velit. Nullam varius maximus mauris. Nulla gravida quam et suscipit mollis. In tempor nisl sit amet gravida lacinia. Duis ut ipsum dictum, venenatis leo nec, volutpat turpis. Suspendisse vehicula libero at metus finibus porttitor. Fusce vehicula justo mi, auctor tristique enim fermentum sit amet. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras eget volutpat dui. Pellentesque sollicitudin malesuada augue, in sollicitudin nisi efficitur ut.",
                postDate = "11 de mayo del 2020, 11:30 a.m." ,
                isLiked = true,
                isBookmarked = false,
                likesCount = 15,
                username = "Username",
                onReturn = {
                    navController.popBackStack()
                },
                onImageClick = { /*TODO*/ },
                onPostUserClick = { /*TODO*/ },
                onLike = { _, _ -> },
                onBookmark = { _, _ -> },
                onSendComment = {}
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
    object SearchScreen: AppScreens("search_screen")
    object CreatePostScreen: AppScreens("create_post_screen")
    object OpenPostScreen: AppScreens("open_post_screen")
}