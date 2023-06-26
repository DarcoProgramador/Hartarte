package com.proyecpg.hartarte.ui.screens.post.open

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.unit.Dp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenPostScreen(
    postId: String,
    postImages: List<String>,
    postUsername: String,
    postUserPic: String,
    postUserFollowers: Int,
    title: String,
    description: String,
    isLiked: Boolean,
    isBookmarked: Boolean,
    likesCount: Int,
    username: String,
    onReturn: () -> Unit,
    onImageClick: () -> Unit,
    onPostUserClick: () -> Unit,
    onLike : (String, Boolean) -> Unit,
    onBookmark : (String, Boolean) -> Unit
){
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier,
        topBar = {
            OpenPostTopAppBar(
                userName = postUsername,
                scrollBehavior= scrollBehavior,
                onClick = onReturn
            )
        }
    ) { innerPadding ->
        OpenPostScreenContent(
            paddingValues = innerPadding,
            postId = postId,
            postImages = postImages,
            postUser = Triple(postUserPic, postUserPic, postUserFollowers), //Firs: URL, second: name
            postInfo = Pair(title, description),
            postStatistics = Triple(isLiked, likesCount, isBookmarked),
            username = username,
            onImageClick = onImageClick,
            onPostUserClick = onPostUserClick,
            onLike = onLike,
            onBookmark = onBookmark
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenPostTopAppBar(
    userName: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onClick: () -> Unit
){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Publicación de $userName",
                color = MaterialTheme.colorScheme.primary
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onClick
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Return icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OpenPostScreenContent(
    paddingValues: PaddingValues,
    postId: String,
    postImages: List<String>,
    postUser: Triple<String, String, Int>, //First: URL, second: name
    postInfo: Pair<String, String>, //First: title, second: description
    postStatistics: Triple<Boolean, Int, Boolean>, //First: isLiked, second: followers, isBookmarked
    username: String,
    onImageClick: () -> Unit,
    onPostUserClick: () -> Unit,
    onLike : (String, Boolean) -> Unit,
    onBookmark : (String, Boolean) -> Unit
){
    var isScrolled = false
    val pagerState = rememberPagerState(initialPage = 0)
    val animatedHeight: Dp by animateDpAsState(targetValue = if (isScrolled) 220.dp else 110.dp)

    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    ){
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(animatedHeight)
            ) {
                HorizontalPager(
                    count = postImages.size,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable {
                            onImageClick()
                        },
                    state = pagerState,
                    verticalAlignment = Alignment.CenterVertically
                ) { page ->

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(postImages[page])
                            .crossfade(true)
                            .scale(Scale.FILL)
                            .build(),
                        contentDescription = "Carousel image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopEnd
                    ){
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Delete image")
                        }
                    }
                }
            }

            //Current image
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(postImages.size){
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(shape = CircleShape)
                            .size(5.dp)
                            .background(if (pagerState.currentPage == it) Color.DarkGray else Color.LightGray)
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 17.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                PostUserInfo(postUser = postUser, onPostUserClick = onPostUserClick)
            }
        }

        item {
            PostInfo(
                postId = postId,
                postInfo = postInfo,
                postStatistics = postStatistics,
                onLike = onLike,
                onBookmark = onBookmark
            )
        }
    }
}

@Composable
fun PostUserInfo(
    postUser: Triple<String, String, Int>,
    onPostUserClick: () -> Unit
){
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .clickable {
                onPostUserClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){
        AsyncImage(
            model = postUser.first,
            contentDescription = "User avatar",
            modifier = Modifier
                .size(35.dp)
                .clip(shape = CircleShape)
        )

        Spacer(modifier = Modifier.size(10.dp))

        Text(
            text = postUser.second
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = "${postUser.third} seguidores",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun PostInfo(
    postId: String,
    postInfo: Pair<String, String>, //First: title, second: description
    postStatistics: Triple<Boolean, Int, Boolean>, //First: isLiked, second: followers, isBookmarked
    onLike : (String, Boolean) -> Unit,
    onBookmark : (String, Boolean) -> Unit
){
    var liked by rememberSaveable(key=postId){ mutableStateOf(postStatistics.first) }
    var likeCount by rememberSaveable(key=postId){ mutableStateOf(postStatistics.second) }
    var bookmarked by rememberSaveable(key=postId){ mutableStateOf(postStatistics.third) }

    Column {
        Text(
            text = postInfo.first,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Divider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outline)

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
        ){
            item {
                Text(
                    text = postInfo.second,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp
                )
            }
        }

        /* TODO: Hacer que muestre la fecha de publicación */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "11 de mayo del 2020, 11:30 a.m.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }

        Divider(modifier = Modifier.padding(top = 10.dp), color = MaterialTheme.colorScheme.outline)

        //Icon buttons
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {

                        if (liked)
                            likeCount--
                        else
                            likeCount++

                        liked = !liked

                        onLike(postId, liked) /* TODO: Cambiar despues a event */
                    }
                ) {
                    if(liked){
                        Icon(
                            painter = painterResource(id = R.drawable.ic_favorite),
                            contentDescription = "Favorite",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    else{
                        Icon(
                            painter = painterResource(id = R.drawable.ic_favorite_border),
                            contentDescription = "Favorite",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.size(5.dp))

                Text(text = likeCount.toString(), color = MaterialTheme.colorScheme.primary)
            }
            IconButton(
                onClick = {
                    bookmarked = !bookmarked

                    onBookmark(postId, bookmarked) /* TODO: Cambiar despues a event */
                }
            ) {
                if(bookmarked){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bookmark),
                        contentDescription = "Bookmark",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                else{
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bookmark_border),
                        contentDescription = "Bookmark",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Divider(modifier = Modifier.padding(bottom = 10.dp), color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
fun Comments(){

}

/*@Preview
@Composable
fun PreviewOpenPostScreen(){
    HartarteTheme {
        Box(modifier = Modifier.fillMaxSize()){
            OpenPostScreen(
                onReturn = {},
                onImageClick = {}
            )
        }
    }
}*/

@Preview(showBackground = true)
@Composable
fun PreviewPostUserInfo(){
    HartarteTheme {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
        ){
            PostUserInfo(
                postUser = Triple(
                "https://cdn.discordapp.com/attachments/1029844385237569616/1116569644745097320/393368.png",
                "User",
                155
                ),
                onPostUserClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPostInfo(){
    HartarteTheme {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 10.dp)
        ){
            PostInfo(
                postId = "postInfo",
                postInfo = Pair(
                    "Post title",
                    "Description description description description description description description description description description description description description description description description description description description description description description description description description description description description description."
                ),
                postStatistics = Triple(true, 15, false),
                onLike = { _, _ -> },
                onBookmark = { _, _ -> }
            )
        }
    }
}

@Composable
fun PreviewComments(){
    HartarteTheme {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 10.dp)
        ){
            Comments()
        }
    }
}