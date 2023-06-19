package com.proyecpg.hartarte.ui.screens.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyecpg.hartarte.ui.theme.HartarteTheme

@Composable
fun UserScreen(
    paddingValues: PaddingValues
){
    val lazyListState = rememberLazyListState()

    Column(
        modifier = Modifier.padding(paddingValues)
    ){
        Box(modifier = Modifier){
            UserCard(
                image = "https://cdn.discordapp.com/attachments/1029844385237569616/1116569644745097320/393368.png",
                username = "Username",
                userDescription = "Esta descripción tiene activado un ellipsis y un límite de 3 líneas para la descripción con el fin de que no se vea muy largo todo.",
                userFollowers = 100,
                userFollows = 100,
                isFollowed = false,
                lazyListState = lazyListState
            )
        }
        LazyColumn(
            modifier = Modifier,
            state = lazyListState
        ){
            //Posts
        }
    }
}

//Revisa si está scrolleado para colapasar un elemento
val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 1 || firstVisibleItemScrollOffset > 1

@Preview
@Composable
fun PreviewUserScreen(){
    HartarteTheme {
        Box(modifier = Modifier.padding(all = 10.dp)){
            UserScreen(PaddingValues())
        }
    }
}