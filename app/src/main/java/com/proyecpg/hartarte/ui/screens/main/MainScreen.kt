package com.proyecpg.hartarte.ui.screens.main

import android.content.res.Configuration
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.google.accompanist.pager.ExperimentalPagerApi
import com.proyecpg.hartarte.data.DataStoreUtil
import com.proyecpg.hartarte.ui.components.Post
import com.proyecpg.hartarte.ui.components.SearchBar
import com.proyecpg.hartarte.ui.components.SideBar
import com.proyecpg.hartarte.ui.theme.HartarteTheme
import com.proyecpg.hartarte.ui.theme.ThemeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    state: MainState,
    viewModel: MainViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel,
    onCreatePost: () -> Unit,
    dataStoreUtil: DataStoreUtil
    //onPostClick
    //onHome ?
    //onBookmark ?
    //onUser ?
){
    //Variables de estado
    var lazyListState = rememberLazyListState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedNavigationIndex by remember { mutableStateOf(0) }
    val navigationBarItems = remember { NavigationBarItems.values() }



    val navigationTitle = listOf("Inicio", "Guardados", "Perfil")
    val scope = rememberCoroutineScope()
    var currentNightMode = LocalContext.current.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

    ModalNavigationDrawer(
        drawerContent = {
            SideBar(
                username = "Username",
                "https://cdn.discordapp.com/attachments/1029844385237569616/1116569644745097320/393368.png",
                onUserCardClick = {
                    selectedNavigationIndex = 2
                    scope.launch { drawerState.close() }
                },
                dataStoreUtil = dataStoreUtil,
                themeViewModel = themeViewModel
            )
        },
        drawerState = drawerState
    ) {
        Scaffold(
            modifier = Modifier.padding(all = 12.dp),
            topBar = {
                Column {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = navigationTitle[selectedNavigationIndex],
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch { drawerState.open() }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    )

                    SearchBar(lazyListState)

                    Spacer(modifier = Modifier.size(5.dp))
                }
            },
            bottomBar = {
                AnimatedNavigationBar(
                    modifier = Modifier.height(65.dp),
                    selectedIndex = selectedNavigationIndex,
                    barColor = MaterialTheme.colorScheme.primary,
                    ballColor = MaterialTheme.colorScheme.primary,
                    ballAnimation = Parabolic(tween(300)),
                    indentAnimation = Height(tween(300)),
                    cornerRadius = shapeCornerRadius(cornerRadius = 30.dp)
                ) {
                    navigationBarItems.forEach { item ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(shape = RoundedCornerShape(30.dp))
                                .noRippleClickable {
                                    selectedNavigationIndex = item.ordinal
                                }
                        ){
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = if (selectedNavigationIndex == item.ordinal)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.inversePrimary
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.size(60.dp),
                    shape = CircleShape,
                    onClick = {
                        onCreatePost
                        /* TODO: Llamar a la screen para crear posts */
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Write a post"
                    )
                }
            }
        ){ innerPadding ->
            MainScreenContent(
                innerPadding = innerPadding,
                viewModel = viewModel
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreenContent(
    innerPadding: PaddingValues,
    viewModel: MainViewModel
){
    //Posts
    val pagingPosts = viewModel.posts.collectAsLazyPagingItems()
    val refresh = pagingPosts.loadState.refresh
    val append = pagingPosts.loadState.append

    LazyColumn(
        modifier = Modifier.padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = pagingPosts){ post ->
            post?.let{
                Post(
                    images =
                    listOf(
                        "https://cdn.discordapp.com/attachments/1109581677199634522/1109581830883127406/576294.png",
                        "https://cdn.discordapp.com/attachments/1109581677199634522/1109581862520766484/576296.png",
                        "https://cdn.discordapp.com/attachments/1109581677199634522/1109581879872585859/576295.png"
                    ),
                    username = it.user?.name ?: "",
                    userPic = it.user?.photo ?: "",
                    title = it.titulo?:"",
                    description = it.descripcion?:"",
                    isLiked = true,
                    isBookmarked = false,
                    likesCount = it.likes?.toInt() ?: 0
                )
            }
        }
        pagingPosts.loadState.apply {
            when {
                refresh is LoadState.Loading -> {

                }
                refresh is Error -> {

                }
                append is LoadState.Loading -> {

                }
                append is Error -> {

                }
            }
        }
    }
}
enum class NavigationBarItems(val icon: ImageVector){
    Home(icon = Icons.Default.Home),
    Bookmark(icon = Icons.Default.Bookmark),
    User(icon = Icons.Default.Person)
}

//Elimina efecto de clicarse
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource()}
    ){
        onClick()
    }
}

/*Revisa si está scrolleado para colapasar un elemento
val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0
*/

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewMainScreen(){
    HartarteTheme {
        MainScreen(
            state = MainState(false),
            viewModel = hiltViewModel(),
            onCreatePost = {},
            themeViewModel = ThemeViewModel(),
            dataStoreUtil = DataStoreUtil(context = LocalContext.current)
        )
    }
}