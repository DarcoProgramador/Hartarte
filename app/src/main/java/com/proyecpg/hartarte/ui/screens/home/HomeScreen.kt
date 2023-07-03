package com.proyecpg.hartarte.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.pager.ExperimentalPagerApi
import com.proyecpg.hartarte.R
import com.proyecpg.hartarte.ui.components.Post
import com.proyecpg.hartarte.ui.theme.HartarteTheme

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    viewModel: HomeViewModel,
    onPostClick: () -> Unit
) {
    HomeScreenContent(paddingValues, viewModel, onPostClick)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreenContent(
    innerPadding: PaddingValues,
    viewModel: HomeViewModel,
    onPostClick: () -> Unit
){
    //Posts
    val pagingPosts = viewModel.posts.collectAsLazyPagingItems()
    val refresh = pagingPosts.loadState.refresh
    val append = pagingPosts.loadState.append

    LazyColumn(
        modifier = Modifier.padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        items(items = pagingPosts){ post ->
            post?.let{
                val postId = it.postId?:""
                val liked = it.liked?:false
                it.images?.let { it1 ->
                    Post(
                        postId = postId,
                        images = it1.toList(),
                        username = it.user?.name ?: "",
                        userPic = it.user?.photo ?: "",
                        title = it.titulo?:"",
                        description = it.descripcion?:"",
                        isLiked = liked,
                        isBookmarked = it.bookmarked?:false,
                        likesCount = it.likes?.toInt() ?: 0,
                        onLike = viewModel::doLike,
                        onBookmark = viewModel::doBookmark,
                        onPostClick = onPostClick
                    )
                }
            }
        }
        pagingPosts.loadState.apply {
            when {
                refresh is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            LoadingItem()
                        }
                    }
                }
                refresh is Error -> {
                    item {
                        ErrorItem()
                    }
                }
                append is LoadState.Loading -> {
                    item {
                        LoadingItem()
                    }
                }
                append is Error -> {
                    item {
                        ErrorItem()
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingItem(){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 6.dp
        )
    }
}

@Composable
fun ErrorItem(){
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        border = null,
        content = {
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = "Placeholder image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            )

            // Current image
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(shape = CircleShape)
                        .size(5.dp)
                        .background(Color.DarkGray)
                )
            }

            // User
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 17.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(R.drawable.user_placeholder),
                    contentDescription = "Placeholder image",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(shape = CircleShape)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(text = "Username")
            }

            //Card information
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 17.dp)
            ){
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "Error al cargar el post",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Text(
                    text = "Ha ocurrido un error inesperadamente al cargar el post.",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )
            }

            // Icons
            //Icon buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 3.dp, bottom = 5.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_favorite_border),
                            contentDescription = "Favorite"
                        )
                    }

                    Spacer(modifier = Modifier.size(5.dp))

                    Text(text = "0")
                }
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bookmark_border),
                        contentDescription = "Bookmark"
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewErrorItem(){
    HartarteTheme {
        Box(modifier = Modifier.padding(10.dp)){
            ErrorItem()
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen(){
    HomeScreen(
        paddingValues = PaddingValues(),
        viewModel = hiltViewModel(),
        onPostClick = {}
    )
}