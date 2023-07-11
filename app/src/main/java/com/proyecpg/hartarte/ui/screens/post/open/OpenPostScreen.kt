package com.proyecpg.hartarte.ui.screens.post.open

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.proyecpg.hartarte.domain.model.Comment
import com.proyecpg.hartarte.ui.components.CommentComponent
import com.proyecpg.hartarte.ui.model.UserUI
import com.proyecpg.hartarte.ui.theme.HartarteTheme
import com.proyecpg.hartarte.utils.Constants.TAG

@Composable
fun OpenPostScreen(
    postInfo: OpenPostArgs,
    currentUserUID : String,
    currentUserUI : UserUI,
    username: String,
    comments : List<Comment>,
    onReturn: () -> Unit,
    onImageClick: (Array<String>) -> Unit,
    onPostUserClick: () -> Unit,
    onLike : (String, Boolean) -> Unit,
    onBookmark : (String, Boolean) -> Unit,
    onSendComment: (String) -> Unit
){

    var comment by remember{ mutableStateOf("") }

    Scaffold(
        modifier = Modifier,
        topBar = {
            OpenPostTopAppBar(
                postTitle = postInfo.postTitle,
                onClick = onReturn
            )
        }
    ) { innerPadding ->

        comment = openPostScreenContent(
            paddingValues = innerPadding,
            postId = postInfo.postId,
            postImages = postInfo.postImages,
            postUser = Pair(postInfo.postUserPic, postInfo.postUsername), //Firs: URL, second: name
            postInfo = Triple(postInfo.postTitle, postInfo.postDescription, postInfo.postDate),
            postStatistics = Triple(postInfo.isLiked, postInfo.likesCount, postInfo.isBookmarked),
            username = username,
            currentUserUID = currentUserUID,
            currentUserUI = currentUserUI,
            comment = comment,
            onImageClick = onImageClick,
            onPostUserClick = onPostUserClick,
            onLike = onLike,
            onBookmark = onBookmark,
            onSendComment = onSendComment,
            comments = comments
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenPostTopAppBar(
    postTitle: String,
    onClick: () -> Unit
){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = postTitle,
                color = MaterialTheme.colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        },
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp),
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
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun openPostScreenContent(
    paddingValues: PaddingValues,
    postId: String,
    postImages: List<String>,
    postUser: Pair<String, String>, //First: URL, second: name, third: followers
    postInfo: Triple<String, String, String>, //First: title, second: description
    postStatistics: Triple<Boolean, Int, Boolean>, //First: isLiked, second: likes, isBookmarked
    username: String,
    currentUserUID: String,
    currentUserUI: UserUI,
    comment: String,
    comments: List<Comment>,
    onImageClick: (Array<String>) -> Unit,
    onPostUserClick: () -> Unit,
    onLike : (String, Boolean) -> Unit,
    onBookmark : (String, Boolean) -> Unit,
    onSendComment: (String) -> Unit
): String {
    val pagerState = rememberPagerState(initialPage = 0)
    var commentText by remember{ mutableStateOf(comment) }
    val userComments  = remember { mutableStateListOf<Comment>() }

    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
    ){
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(370.dp),
                shape = RoundedCornerShape(0.dp)
            ) {
                HorizontalPager(
                    count = postImages.size,
                    modifier = Modifier
                        .fillMaxSize()
                       // .background(MaterialTheme.colorScheme.primaryContainer),
                        .background(
                            Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black))
                        ),

                    state = pagerState,
                    verticalAlignment = Alignment.CenterVertically
                ) { page ->
                    val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffset
                    val imageSize by animateFloatAsState(
                        targetValue = if ( pageOffset != 0.0f) 0.75f else 1f,
                        animationSpec = tween(durationMillis = 300)
                    )
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(postImages[page])
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .crossfade(true)
                            .scale(Scale.FILL)
                            .build(),
                        contentDescription = "Carousel image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                var arrayImages = postImages.toTypedArray()

                                for (i in arrayImages.indices) {
                                    Log.e(TAG,arrayImages[i])
                                }

                                onImageClick(arrayImages)
                            }
                            .padding(5.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .graphicsLayer {
                                scaleX = imageSize
                                scaleY = imageSize
                            },
                        contentScale = ContentScale.Crop
                    )
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 5.dp),
                verticalArrangement = Arrangement.Center
            ) {
                PostUserInfo(postUser = postUser, onPostUserClick = onPostUserClick)

                Spacer(modifier = Modifier.height(10.dp))

                PostInfo(
                    postId = postId,
                    postInfo = postInfo,
                    postStatistics = postStatistics,
                    onLike = onLike,
                    onBookmark = onBookmark
                )

                commentText = customTextInputField(username = username, comment = commentText,
                    onSendComment = onSendComment,
                    addUserComemnt = { commentText ->
                        userComments.add(Comment(
                            comment = commentText,
                            uid = currentUserUID,
                            username = currentUserUI.username,
                            photo = currentUserUI.photo
                        ))
                    })

                Spacer(modifier = Modifier.height(5.dp))

                UserComments(userComments = userComments)

                Comments(comments = comments)
            }
        }
    }

    return commentText
}

@Composable
fun PostUserInfo(
    postUser: Pair<String, String>,
    onPostUserClick: () -> Unit
){
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .clickable {
                onPostUserClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(postUser.first)
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder)
                .crossfade(true)
                .scale(Scale.FILL)
                .build(),
            contentDescription = "User avatar",
            modifier = Modifier
                .size(35.dp)
                .clip(shape = CircleShape)
        )

        Spacer(modifier = Modifier.size(10.dp))

        Text(
            text = postUser.second
        )
    }
}

@Composable
fun PostInfo(
    postId: String,
    postInfo: Triple<String, String, String>, //First: title, second: description
    postStatistics: Triple<Boolean, Int, Boolean>, //First: isLiked, second: followers, isBookmarked
    onLike : (String, Boolean) -> Unit,
    onBookmark : (String, Boolean) -> Unit
){
    val liked by remember{ derivedStateOf { mutableStateOf(postStatistics.first)} }
    val likeCount by remember{ derivedStateOf {mutableStateOf(postStatistics.second)} }
    val bookmarked by remember{ derivedStateOf { mutableStateOf(postStatistics.third) } }

    LaunchedEffect(key1 = postStatistics.first){
        liked.value = postStatistics.first
    }

    LaunchedEffect(key1 = postStatistics.second){
        likeCount.value = postStatistics.second
    }

    LaunchedEffect(key1 = postStatistics.third){
        bookmarked.value = postStatistics.third
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = postInfo.first,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Divider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outline)

        Column(modifier = Modifier.fillMaxWidth()){
            Text(
                text = postInfo.second,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 16.sp
            )
        }

        /* TODO: Hacer que muestre la fecha de publicación */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = postInfo.third,
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

                        if (liked.value)
                            likeCount.value--
                        else
                            likeCount.value++

                        liked.value = !liked.value

                        onLike(postId, liked.value) /* TODO: Cambiar despues a event */
                    }
                ) {
                    if(liked.value){
                        Icon(
                            painter = painterResource(id = R.drawable.ic_favorite),
                            contentDescription = "Favorite",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    else{
                        Icon(
                            painter = painterResource(id = R.drawable.ic_favorite_border),
                            contentDescription = "Favorite",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.size(5.dp))

                Text(text = likeCount.value.toString(), color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            IconButton(
                onClick = {
                    bookmarked.value = !bookmarked.value

                    onBookmark(postId, bookmarked.value) /* TODO: Cambiar despues a event */
                }
            ) {
                if(bookmarked.value){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bookmark),
                        contentDescription = "Bookmark",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                else{
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bookmark_border),
                        contentDescription = "Bookmark",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        Divider(modifier = Modifier.padding(bottom = 10.dp), color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
fun customTextInputField(
    username: String,
    comment: String,
    addUserComemnt : (String) -> Unit,
    onSendComment: (String) -> Unit
): String {

    var text by remember { (mutableStateOf(comment)) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(5.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        value = text,
        onValueChange = {
            if (it.length <= 500){
                text = it
            }
        },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 16.sp
        ),
        placeholder = {
            Text(
                text = "Escribe un comentario..."
            )
        },
        trailingIcon = {
            if (text.isNotEmpty()){
                IconButton(
                    onClick = {
                        onSendComment(text)
                        addUserComemnt(text)
                        text = ""
                    }
                ){
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Enviar comentario",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        },
        maxLines = 10
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = "Estás comentando como $username",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = text.length.toString() + "/500",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }

    return text
}

@Composable
fun UserComments(userComments : List<Comment>){
    if(userComments.isEmpty()){
        return
    }
    Column(modifier = Modifier) {
        for (comment in userComments.reversed()){
            if (comment.comment.isNullOrBlank()){
                continue
            }
            CommentComponent(
                image = comment.photo?:"",
                username = comment.username?:"",
                description = comment.comment,
                date = "",
                onPostUserClick = {}
            )
        }
    }
}

@Composable
fun Comments(comments : List<Comment>){
    if(comments.isEmpty()){
        return
    }
    Column(modifier = Modifier) {
        for (comment in comments){
            if (comment.comment.isNullOrBlank()){
                continue
            }
            CommentComponent(
                image = comment.photo?:"",
                username = comment.username?:"",
                description = comment.comment,
                date = "",
                onPostUserClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPostUserInfo(){
    HartarteTheme {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
        ){
            PostUserInfo(
                postUser = Pair(
                "https://cdn.discordapp.com/attachments/1029844385237569616/1116569644745097320/393368.png",
                "User"
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
                postInfo = Triple(
                    "Post title",
                    "Description description description description description description description description description description description description description description description description description description description description description description description description description description description description description.",
                    "11 de mayo del 2020, 11:30 a.m."
                ),
                postStatistics = Triple(true, 15, false),
                onLike = { _, _ -> },
                onBookmark = { _, _ -> }
            )
        }
    }
}

@Preview
@Composable
fun PreviewCustomTextInputField(){
    HartarteTheme {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
        ){
            customTextInputField(
                username = "User",
                comment = "Comment attempt",
                onSendComment = {},
                addUserComemnt = {}
            )
        }
    }
}

@Preview
@Composable
fun PreviewOpenPostScreen(){
    HartarteTheme {
        Box(modifier = Modifier.fillMaxSize()){
            OpenPostScreen(
                postInfo = OpenPostArgs(
                    postId = "01",
                    postImages = listOf
                        (
                        "https://cdn.discordapp.com/attachments/1109581677199634522/1109581830883127406/576294.png",
                        "https://cdn.discordapp.com/attachments/1109581677199634522/1109581862520766484/576296.png",
                        "https://cdn.discordapp.com/attachments/1109581677199634522/1109581879872585859/576295.png"
                    ),
                    postUsername = "HartarteUser",
                    postUserPic = "https://cdn.discordapp.com/attachments/1029844385237569616/1116569644745097320/393368.png",
                    postTitle = "Título de ejemplo",
                    postDescription = "Lorem ipsum.",
                    postDate = "11 de mayo del 2020, 11:30 a.m." ,
                    isLiked = true,
                    isBookmarked = false,
                    likesCount = 15
                ),
                currentUserUID = "",
                username = "Username",
                onReturn = { /*TODO*/ },
                onImageClick = { /*TODO*/ },
                onPostUserClick = { /*TODO*/ },
                onLike = { _, _ -> },
                onBookmark = { _, _ -> },
                onSendComment = {},
                comments = listOf(Comment()),
                currentUserUI = UserUI()
            )
        }
    }
}