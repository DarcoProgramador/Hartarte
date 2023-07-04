package com.proyecpg.hartarte.ui.screens.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyecpg.hartarte.ui.components.Post
import com.proyecpg.hartarte.ui.model.UserUI
import com.proyecpg.hartarte.ui.theme.HartarteTheme

@Composable
fun UserScreen(
    paddingValues: PaddingValues,
    onProcessUSer : (UserEvent) -> Unit,
    userEditState : UserState,
    userState : UserUI
){
    val lazyListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ){
        Box(modifier = Modifier){
            UserCard(
                userImage = userState.photo,
                username = userState.username?:"User",
                userDescription = userState.descripcion,
                userEditState = userEditState,
                lazyListState = lazyListState,
                onSendDescription = onProcessUSer
            )
        }

        Divider(
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        LazyColumn(
            modifier = Modifier,
            state = lazyListState
        ){
            item {
                for(x in 1 .. 20){
                    Post(
                        postId = x.toString(),
                        images = listOf
                            (
                            "https://cdn.discordapp.com/attachments/1109581677199634522/1109581830883127406/576294.png",
                            "https://cdn.discordapp.com/attachments/1109581677199634522/1109581862520766484/576296.png",
                            "https://cdn.discordapp.com/attachments/1109581677199634522/1109581879872585859/576295.png"
                        ),
                        username = "TheJosuep",
                        userPic = "https://cdn.discordapp.com/attachments/1029844385237569616/1116569644745097320/393368.png",
                        title = "Título de ejemplo",
                        description = "Esta descripción tiene activado un ellipsis y un límite de 3 líneas para la descripción con el fin de que no se vea muy largo todo.",
                        isBookmarked = true,
                        isLiked = false,
                        likesCount = 40,
                        onLike = { _: String, _: Boolean -> run {} },
                        onBookmark = { _: String, _: Boolean -> run {} },
                        onPostClick = {}
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

//Revisa si está scrolleado para colapasar un elemento
val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 3 || firstVisibleItemScrollOffset > 3

@Preview
@Composable
fun PreviewUserScreen(){
    HartarteTheme {
        Box(modifier = Modifier.padding(all = 10.dp)){
            UserScreen(
                paddingValues = PaddingValues(),
                onProcessUSer = {},
                userEditState = UserState(),
                userState = UserUI(username = "Prueba", descripcion = "descipcion")
            )
        }
    }
}