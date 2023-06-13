package com.proyecpg.hartarte.ui.screens.main

import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.google.accompanist.pager.ExperimentalPagerApi
import com.proyecpg.hartarte.ui.components.Post
import com.proyecpg.hartarte.ui.theme.HartarteTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    state: MainState,
    viewModel: MainViewModel = hiltViewModel(),
    onCreatePost: () -> Unit
    //onPostClick
    //onHome ?
    //onBookmark ?
    //onUser ?
){
    //val lazyListState = rememberLazyListState()

    //Variables de estado
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedNavigationIndex by remember { mutableStateOf(0) }
    val navigationBarItems = remember { NavigationBarItems.values() }
    var darkThemeActive = false

    //Posts
    val pagingPosts = viewModel.posts.collectAsLazyPagingItems()
    val refresh = pagingPosts.loadState.refresh
    val append = pagingPosts.loadState.append

    val navigationTitle = listOf("Inicio", "Guardados", "Perfil")
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))

                Card(
                    modifier = Modifier
                        .height(100.dp)
                        .padding(all = 5.dp)
                        .clickable {
                            selectedNavigationIndex = 2
                            scope.launch { drawerState.close() }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = "https://cdn.discordapp.com/attachments/1029844385237569616/1116569644745097320/393368.png",
                            contentDescription = "Username",
                            modifier = Modifier
                                .size(85.dp, 85.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(Modifier.width(12.dp))

                        Text(
                            text = "Username",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                if (darkThemeActive){
                    NavigationDrawerItem(
                        label = { Text(text = "Cambiar al modo claro") },
                        selected = false,
                        onClick = { darkThemeActive = !darkThemeActive },
                        icon = { Icon(imageVector = Icons.Outlined.LightMode, contentDescription = "Light mode") }
                    )
                }
                else{
                    NavigationDrawerItem(
                        label = { Text(text = "Cambiar al modo oscuro") },
                        selected = false,
                        onClick = { darkThemeActive = !darkThemeActive },
                        icon = { Icon(imageVector = Icons.Outlined.DarkMode, contentDescription = "Dark mode") }
                    )
                }

                Spacer(Modifier.height(12.dp))

                NavigationDrawerItem(
                    label = { Text(text = "Sobre la app") },
                    selected = false,
                    onClick = { /* TODO: Mostrar ventana de info */ },
                    icon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = "About us") }
                )

                Spacer(Modifier.height(12.dp))

                NavigationDrawerItem(
                    label = { Text(text = "Cerrar sesión") },
                    selected = false,
                    onClick = { /* TODO: Cerrar sesión */ },
                    icon = { Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Log out") }
                )
            }
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
                    //SearchBar(lazyListState = lazyListState)
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
            innerPadding
            /*
            val posts = listOf(
                "Título de ejemplo 1",
                "Título de ejemplo 2",
                "Título de ejemplo 3"
            )

            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                state = lazyListState
            ) {

                items(posts.size) { post ->
                    Post(
                        images = listOf
                            (
                            "https://cdn.discordapp.com/attachments/1109581677199634522/1109581830883127406/576294.png",
                            "https://cdn.discordapp.com/attachments/1109581677199634522/1109581862520766484/576296.png",
                            "https://cdn.discordapp.com/attachments/1109581677199634522/1109581879872585859/576295.png"
                        ),
                        username = "TheJosuep",
                        userPic = "https://cdn.discordapp.com/attachments/1029844385237569616/1116569644745097320/393368.png",
                        title = posts[post],
                        description = "Esta descripción tiene activado un ellipsis y un límite de 3 líneas para la descripción con el fin de que no se vea muy largo todo.",
                        isBookmarked = true,
                        isLiked = false,
                        likesCount = 40
                    )
                }
                item {
                    CircularProgressIndicator()
                }
            }

             */

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
                            title = it.titulo,
                            description = it.descripcion,
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
                        refresh is Error -> print(refresh)
                        append is LoadState.Loading -> {

                        }
                        append is Error -> print(append)
                    }
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
            onCreatePost = {}
        )
    }
}