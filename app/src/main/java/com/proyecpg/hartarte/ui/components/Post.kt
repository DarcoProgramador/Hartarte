package com.proyecpg.hartarte.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.proyecpg.hartarte.R
import com.proyecpg.hartarte.ui.theme.HartarteTheme

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Post(
    postId: String,
    images: List<String>,
    username: String,
    userPic: String,
    title: String,
    description: String,
    isLiked: Boolean,
    isBookmarked: Boolean,
    likesCount: Int,
    onLike : (String, Boolean) -> Unit,
    onBookmark : (String, Boolean) -> Unit,
    onPostClick: () -> Unit
){
    val pagerState = rememberPagerState(initialPage = 0)

    val liked by remember{ derivedStateOf { mutableStateOf(isLiked) } }
    val bookmarked by remember{ derivedStateOf{ mutableStateOf(isBookmarked) } }
    var likeCount by remember{ mutableStateOf(likesCount) }

    LaunchedEffect(key1 = isLiked){
        liked.value = isLiked
    }

    LaunchedEffect(key1 = isBookmarked){
        bookmarked.value = isBookmarked
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        border = null,
        content = {

            HorizontalPager(
                count = images.size,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                state = pagerState,
                verticalAlignment = Alignment.CenterVertically
            ) { page ->

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(images[page])
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .crossfade(true)
                        .scale(Scale.FILL)
                        .build(),
                    contentDescription = "Carousel image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            //Current image
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(images.size){
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(shape = CircleShape)
                            .size(5.dp)
                            .background(if (pagerState.currentPage == it) Color.DarkGray else Color.LightGray)
                    )
                }
            }

            Column(modifier = Modifier
                .clickable {
                    onPostClick()
                }
            ){
                //User
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 17.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    //Clickable segment in the row
                    Row(
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .clickable {
                                /* TODO */
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        AsyncImage(
                            model = userPic,
                            contentDescription = "User avatar",
                            placeholder = painterResource(id = R.drawable.user_placeholder),
                            error = painterResource(id = R.drawable.user_placeholder),
                            modifier = Modifier
                                .size(30.dp)
                                .clip(shape = CircleShape)
                        )

                        Spacer(modifier = Modifier.size(10.dp))

                        Text(
                            text = username
                        )
                    }
                }

                //Card information
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 17.dp)
                ){
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Text(
                        text = description,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 3
                    )
                }

                //Icon buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 3.dp, bottom = 5.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {

                                if (liked.value)
                                    likeCount--
                                else
                                    likeCount++

                                liked.value = !liked.value

                                onLike(postId, liked.value) /* TODO: Cambiar despues a event */
                            }
                        ) {
                            if(liked.value){
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_favorite),
                                    contentDescription = "Favorite"
                                )
                            }
                            else{
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_favorite_border),
                                    contentDescription = "Favorite"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.size(5.dp))

                        Text(text = likeCount.toString())
                    }
                    IconButton(
                        onClick = {
                            bookmarked.value = !bookmarked.value

                            onBookmark(postId, bookmarked.value) //TODO: Cambiar despues a event
                        }
                    ) {
                        if(bookmarked.value){
                            Icon(
                                painter = painterResource(id = R.drawable.ic_bookmark),
                                contentDescription = "Bookmark"
                            )
                        }
                        else{
                            Icon(
                                painter = painterResource(id = R.drawable.ic_bookmark_border),
                                contentDescription = "Bookmark"
                            )
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewPost(){
    HartarteTheme {
        Box(
            modifier = Modifier.padding(all = 10.dp)
        ) {
            Post(
                postId = "",
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
        }
    }
}