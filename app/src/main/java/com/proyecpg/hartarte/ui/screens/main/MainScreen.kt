package com.proyecpg.hartarte.ui.screens.main

import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.proyecpg.hartarte.ui.theme.HartarteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    //onHome
    //onBookmark
    //onUser
    //onCreatePost
){
    var selectedIndex by remember { mutableStateOf(0) }
    val navigationBarItems = remember { NavigationBarItems.values() }
    val navigationTitle = listOf(
        "Inicio",
        "Guardados",
        "Perfil"
    )

    Scaffold(
        modifier = Modifier.padding(all = 12.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = navigationTitle[selectedIndex]
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            /* TODO: Abrir pantalla de configuración */
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                }
            )
        },
        bottomBar = {
            AnimatedNavigationBar(
                modifier = Modifier.height(60.dp),
                selectedIndex = selectedIndex,
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
                                selectedIndex = item.ordinal
                            }
                    ){
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = if (selectedIndex == item.ordinal)
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

        //Barra de búsqueda colapsable

        //Contenido
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

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen(){
    HartarteTheme {
        MainScreen()
    }
}