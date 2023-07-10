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
import com.proyecpg.hartarte.ui.screens.AuthViewModel
import com.proyecpg.hartarte.ui.screens.PostSharedEvent
import com.proyecpg.hartarte.ui.screens.PostSharedViewModel
import com.proyecpg.hartarte.ui.screens.main.MainScreen
import com.proyecpg.hartarte.ui.screens.main.MainViewModel
import com.proyecpg.hartarte.ui.screens.post.create.CreatePostScreen
import com.proyecpg.hartarte.ui.screens.post.create.CreatePostScreenViewModel
import com.proyecpg.hartarte.ui.screens.post.open.OpenPostScreen
import com.proyecpg.hartarte.ui.screens.post.open.OpenPostViewModel
import com.proyecpg.hartarte.ui.screens.search.Search
import com.proyecpg.hartarte.ui.screens.search.SearchScreen
import com.proyecpg.hartarte.ui.screens.search.SearchViewModel
import com.proyecpg.hartarte.ui.screens.user.UserViewModel

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
                            navController.navigate(AppScreens.CreatePostScreen.route){
                            }
                        },
                        viewModel = mainViewModel,
                        onLogoutClick = authViewModel::process,
                        onSearchClick = {
                            navController.navigate(AppScreens.SearchScreen.route)
                        },
                        onPostClick = {args ->
                            postSharedViewModel.updatePost(args)
                            navController.navigate(AppScreens.OpenPostScreen.route)
                        },
                        onProcessUser = userViewModel::processUser,
                        onPostSharedProcess = postSharedViewModel::onProcess,
                        userState = userState,
                        userEditState = userEditState,
                        stateLiked = stateLiked,
                        stateBookmarked = stateBookmarked,
                        postUser = userViewModel.postsUser
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
            Search(
                searchBoxState = searchViewModel.searchBoxState,
                paginator = searchViewModel.hitsPaginator
            )

            /*val stateLiked by postSharedViewModel.stateLiked.collectAsStateWithLifecycle()
            val stateBookmarked by postSharedViewModel.stateBookmarked.collectAsStateWithLifecycle()
            SearchScreen(
                viewModel = hiltViewModel(),
                onPostClick = {args ->
                    postSharedViewModel.updatePost(args)
                    navController.navigate(AppScreens.OpenPostScreen.route)
                },
                onReturn = {
                    navController.popBackStack()
                },
                onPostSharedProcess = postSharedViewModel::onProcess,
                stateLiked = stateLiked,
                stateBookmarked = stateBookmarked
            )*/
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

        composable(AppScreens.OpenPostScreen.route)
        {

            val openPostViewModel = hiltViewModel<OpenPostViewModel>()
            val statePost = postSharedViewModel.statePost
            OpenPostScreen(
                postInfo = statePost,
                username = statePost.postUsername,
                onReturn = {
                    navController.popBackStack()
                },
                onImageClick = { /*TODO*/ },
                onPostUserClick = { /*TODO*/ },
                onLike = { postId : String, like : Boolean ->
                    openPostViewModel.doLike(postId, like)
                    postSharedViewModel.onProcess(PostSharedEvent.OnLiked(postId, like))
                },
                onBookmark = { postId : String, bookmark : Boolean ->
                    openPostViewModel.doBookmark(postId, bookmark)
                    postSharedViewModel.onProcess(PostSharedEvent.OnBookmarked(postId, bookmark))
                },
                onSendComment = {}
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
    object MainScreen: AppScreens("main_screen")
    object HomeScreen: AppScreens("home_screen")
    object BookmarkScreen: AppScreens("bookmarks_screen")
    object UserScreen: AppScreens("user_screen")
    object SearchScreen: AppScreens("search_screen")
    object CreatePostScreen: AppScreens("create_post_screen")
    object OpenPostScreen: AppScreens("open_post_screen")
}