package com.proyecpg.hartarte.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.pager.ExperimentalPagerApi
import com.proyecpg.hartarte.ui.components.Post

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    viewModel: HomeViewModel
) {
    HomeScreenContent(paddingValues, viewModel)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreenContent(
    innerPadding: PaddingValues,
    viewModel: HomeViewModel
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
                Post(
                    postId = postId,
                    images = listOf(it.imagen?:""),
                    username = it.user?.name ?: "",
                    userPic = it.user?.photo ?: "",
                    title = it.titulo?:"",
                    description = it.descripcion?:"",
                    isLiked = liked,
                    isBookmarked = it.bookmarked?:false,
                    likesCount = it.likes?.toInt() ?: 0,
                    onLike = viewModel::doLike,
                    onBookmark = viewModel::doBookmark
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

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen(){
    HomeScreen(
        paddingValues = PaddingValues(),
        viewModel = hiltViewModel()
    )
}