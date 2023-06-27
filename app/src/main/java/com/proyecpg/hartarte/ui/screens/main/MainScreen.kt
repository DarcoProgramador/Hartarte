package com.proyecpg.hartarte.ui.screens.main

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.google.accompanist.pager.ExperimentalPagerApi
import com.proyecpg.hartarte.ui.Event
import com.proyecpg.hartarte.ui.components.SearchBar
import com.proyecpg.hartarte.ui.components.SideBar
import com.proyecpg.hartarte.ui.screens.home.HomeScreen
import com.proyecpg.hartarte.ui.screens.login.LoginEvent
import com.proyecpg.hartarte.ui.screens.user.UserScreen
import com.proyecpg.hartarte.ui.theme.HartarteTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onLogoutClick: (LoginEvent) -> Unit,
    onCreatePost: () -> Unit,
    onPostClick: () -> Unit
){
    //Variables de estado
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val state = viewModel.state
    val userState = viewModel.userState.collectAsStateWithLifecycle()

    var selectedNavigationIndex by rememberSaveable(key = "navIndex") { mutableStateOf(0) }
    var isSearchOpened by rememberSaveable(key = "Search") { mutableStateOf(false) }

    HartarteTheme(darkTheme = state.darkThemeValue) {
        ModalNavigationDrawer(
            drawerContent = {
                SideBar(
                    username = userState.value.username.toString(),
                    imageURL = userState.value.photo.toString(),
                    onUserCardClick = {
                        selectedNavigationIndex = 2
                        scope.launch { drawerState.close() }
                    },
                    onCheckedChange = {
                        viewModel.onEvent(Event.SelectedDarkThemeValue(it))
                    },
                    onLogoutClick = onLogoutClick,
                    saveDarkThemeValue = {
                        viewModel.onEvent(Event.SaveDarkThemeValue(it))
                    },
                    state = state,
                    switchState = state.darkThemeValue
                )
            },
            drawerState = drawerState
        ) {
            Scaffold(
                modifier = Modifier
                    .padding(all = 12.dp)
                    .background(color = MaterialTheme.colorScheme.background),
                topBar = {
                    Column {
                        topBar(
                            selectedNavigationIndex = selectedNavigationIndex,
                            isSearchOpened = isSearchOpened,
                            onClick = {
                                scope.launch { drawerState.open() }
                            },
                            onSearch = {
                                isSearchOpened = !isSearchOpened
                            }
                        )

                        SearchBar(isSearchOpened)

                        Spacer(modifier = Modifier.size(5.dp))
                    }
                },
                bottomBar = {
                    selectedNavigationIndex = navBar(selectedNavigationIndex)
                },
                floatingActionButton = {
                    FloatingActionButton(
                        modifier = Modifier.size(60.dp),
                        shape = CircleShape,
                        onClick = onCreatePost
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Write a post"
                        )
                    }
                }
            ){ innerPadding ->

                when(selectedNavigationIndex){
                    0 -> HomeScreen(paddingValues = innerPadding, viewModel = hiltViewModel(), onPostClick = onPostClick)
                    1 -> {  }
                    2 -> UserScreen(paddingValues = innerPadding)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBar(
    selectedNavigationIndex: Int,
    isSearchOpened: Boolean,
    onClick: () -> Unit,
    onSearch: () -> Unit
): Boolean {

    val navigationTitle = listOf("Inicio", "Guardados", "Perfil")

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = navigationTitle[selectedNavigationIndex],
                color = MaterialTheme.colorScheme.primary
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onClick
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        actions = {
            IconButton(
                onClick = onSearch
            ) {
                if (isSearchOpened){
                    Icon(
                        imageVector = Icons.Default.ExpandLess,
                        contentDescription = "Expand less icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                else {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    )

    return isSearchOpened
}

@Composable
fun navBar(
    selectedNavigationIndex: Int
): Int {

    val navigationBarItems = remember { NavigationBarItems.values() }
    var selectedNavIndex by remember { mutableStateOf(selectedNavigationIndex) }

    //Mantiene la variable actualizada con respecto a la original
    LaunchedEffect(selectedNavigationIndex){
        selectedNavIndex = selectedNavigationIndex
    }

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
                        selectedNavIndex = item.ordinal
                    }
            ){
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = if (selectedNavIndex == item.ordinal)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.inversePrimary
                )
            }
        }
    }

    return selectedNavIndex
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

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewMainScreen(){
    HartarteTheme {
        MainScreen(
            hiltViewModel(),
            onCreatePost = {},
            onLogoutClick = {},
            onPostClick = {}
        )
    }
}