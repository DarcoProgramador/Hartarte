package com.proyecpg.hartarte.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.proyecpg.hartarte.ui.screens.AuthViewModel
import com.proyecpg.hartarte.ui.screens.PostSharedEvent
import com.proyecpg.hartarte.ui.screens.PostSharedViewModel
import com.proyecpg.hartarte.ui.screens.main.MainScreen
import com.proyecpg.hartarte.ui.screens.main.MainViewModel
import com.proyecpg.hartarte.ui.screens.post.create.CreatePostScreen
import com.proyecpg.hartarte.ui.screens.post.create.CreatePostScreenViewModel
import com.proyecpg.hartarte.ui.screens.post.open.OpenPostImageScreen
import com.proyecpg.hartarte.ui.screens.post.open.OpenPostScreen
import com.proyecpg.hartarte.ui.screens.post.open.OpenPostViewModel
import com.proyecpg.hartarte.ui.screens.search.SearchScreen
import com.proyecpg.hartarte.ui.screens.search.SearchViewModel
import com.proyecpg.hartarte.ui.screens.user.main.UserViewModel
import com.proyecpg.hartarte.ui.screens.user.open.OpenUserScreen
import com.proyecpg.hartarte.ui.screens.user.open.OpenUserViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NavigationRoot(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel,
    searchViewModel: SearchViewModel
) {
    val state by authViewModel.logged.collectAsStateWithLifecycle()
    val postSharedViewModel : PostSharedViewModel = hiltViewModel()

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
            state.let {
                if (it){
                    val userViewModel = hiltViewModel<UserViewModel>()
                    val userState by userViewModel.userState.collectAsStateWithLifecycle()
                    val userEditState by userViewModel.editUserState.collectAsStateWithLifecycle()
                    val stateLiked by postSharedViewModel.stateLiked.collectAsStateWithLifecycle()
                    val stateBookmarked by postSharedViewModel.stateBookmarked.collectAsStateWithLifecycle()

                    MainScreen(
                        onCreatePost = {
                            navController.navigate(AppScreens.CreatePostScreen.route)
                        },
                        viewModel = mainViewModel,
                        onLogoutClick = authViewModel::process,
                        onSearchClick = {
                            navController.navigate(AppScreens.SearchScreen.route)
                        },
                        onPostClick = {postId ->
                            navController.navigate(AppScreens.OpenPostScreen.route.plus("/${postId}")){
                                launchSingleTop = true
                            }
                        },
                        onProcessUser = userViewModel::processUser,
                        onPostSharedProcess = postSharedViewModel::onProcess,
                        userState = userState,
                        userEditState = userEditState,
                        stateLiked = stateLiked,
                        stateBookmarked = stateBookmarked,
                        postUser = userViewModel.postsUser,
                        onImageClick = {
                        },
                        onUserClick = {userId ->
                            navController.navigate(AppScreens.OpenUserScreen.route.plus("/${userId}")){
                                launchSingleTop = true
                            }
                        }
                    )
                }else{
                    LaunchedEffect(Unit) {
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
                searchBoxState = searchViewModel.searchBoxState,
                paginator = searchViewModel.hitsPaginator,
                statsText = searchViewModel.statsText,
                onPostClick = {postId ->
                    navController.navigate(AppScreens.OpenPostScreen.route.plus("/${postId}"))
                },
                onReturn = { navController.popBackStack() },
                onUserClick = {userId ->
                    navController.navigate(AppScreens.OpenUserScreen.route.plus("/${userId}"))
                }
            )
        }

        composable(AppScreens.CreatePostScreen.route){
            val createPostScreenViewModel  = hiltViewModel<CreatePostScreenViewModel>()
            val stateCreatePost by createPostScreenViewModel.stateCreatePost.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = stateCreatePost.isCreatePostSuccessful) {
                if (stateCreatePost.isCreatePostSuccessful){
                    navController.popBackStack()
                    createPostScreenViewModel.resetState()
                }
            }

            CreatePostScreen(
                onReturn = {
                    navController.popBackStack()
                },
                onProcess = createPostScreenViewModel::process,
                state = stateCreatePost,
                onDismissDialog = {
                    createPostScreenViewModel.hideErrorDialog()
                }
            )
        }

        composable(AppScreens.OpenPostScreen.route.plus("/{postId}"),
            arguments = listOf(navArgument("postId"){type = NavType.StringType})
            )
        { backStackEntry ->

            val openPostViewModel = hiltViewModel<OpenPostViewModel>()
            val postId = backStackEntry.arguments?.getString("postId")?:""
            openPostViewModel.updatePost(postId)
            openPostViewModel.updateComments(postId)
            openPostViewModel.getUser()
            val statePost = openPostViewModel.statePost.collectAsStateWithLifecycle()
            val stateComments = openPostViewModel.stateComments.collectAsStateWithLifecycle()
            val currentUserUI = openPostViewModel.userState.collectAsStateWithLifecycle()
            val currentUserUID = authViewModel.getCurrentUserUID()

            OpenPostScreen(
                postInfo = statePost.value,
                username = statePost.value.postUsername,
                currentUserUID = currentUserUID,
                currentUserUI = currentUserUI.value,
                comments = stateComments.value,
                onReturn = {
                    navController.popBackStack()
                },
                onImageClick = {images ->
                    postSharedViewModel.stateImages.value = images.toList()
                    navController.navigate(AppScreens.OpenPostImageScreen.route)
                },
                onPostUserClick = {userId ->
                    navController.navigate(AppScreens.OpenUserScreen.route.plus("/${userId}"))
                },
                onLike = { postId1 : String, like : Boolean ->
                    postSharedViewModel.onProcess(PostSharedEvent.OnLiked(postId1, like))
                },
                onBookmark = { postId1 : String, bookmark : Boolean ->
                    postSharedViewModel.onProcess(PostSharedEvent.OnBookmarked(postId1, bookmark))
                },
                onSendComment = {comment ->
                    openPostViewModel.addComment(postId, comment)
                }
            )
        }


        composable(AppScreens.OpenPostImageScreen.route){
            OpenPostImageScreen(imagen = postSharedViewModel.stateImages.value)
        }

        composable(AppScreens.OpenUserScreen.route.plus("/{userId}"),
            arguments = listOf(navArgument("userId"){type = NavType.StringType})
        )
        { backStackEntry ->

            val userId = backStackEntry.arguments?.getString("userId")?:""
            val openUserViewModel = hiltViewModel<OpenUserViewModel>()
            openUserViewModel.updateUser(userId)
            openUserViewModel.updateUserPosts(userId)
            val userState by openUserViewModel.userState.collectAsStateWithLifecycle()
            val stateLiked by postSharedViewModel.stateLiked.collectAsStateWithLifecycle()
            val stateBookmarked by postSharedViewModel.stateBookmarked.collectAsStateWithLifecycle()

            OpenUserScreen(
                userState = userState,
                viewModel = openUserViewModel,
                stateLiked = stateLiked,
                stateBookmarked = stateBookmarked,
                onPostClick = {postId ->
                    navController.navigate(AppScreens.OpenPostScreen.route.plus("/${postId}"))
                },
                onPostSharedProcess = postSharedViewModel::onProcess,
                onReturn = {
                    navController.popBackStack()
                },
                onUserClick = {}
            )
        }
    }
}


object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val MAIN = "home_graph"
}

sealed class AppScreens(val route: String){
    object SearchScreen: AppScreens("search_screen")
    object CreatePostScreen: AppScreens("create_post_screen")
    object OpenPostScreen: AppScreens("open_post_screen")
    object OpenPostImageScreen: AppScreens("open_post_image_screen")
    object OpenUserScreen: AppScreens("open_user_screen")
}