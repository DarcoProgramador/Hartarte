package com.proyecpg.hartarte.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.proyecpg.hartarte.ui.components.ErrorItem
import com.proyecpg.hartarte.ui.components.LoadingItem
import com.proyecpg.hartarte.ui.components.Post
import com.proyecpg.hartarte.ui.screens.post.open.OpenPostArgs
import java.text.SimpleDateFormat

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    viewModel: HomeViewModel,
    onPostClick: (OpenPostArgs) -> Unit
) {
    HomeScreenContent(paddingValues, viewModel, onPostClick)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreenContent(
    innerPadding: PaddingValues,
    viewModel: HomeViewModel,
    onPostClick: (OpenPostArgs) -> Unit
){
    //Posts
    val pagingPosts = viewModel.posts.collectAsLazyPagingItems()
    val refresh = pagingPosts.loadState.refresh
    val append = pagingPosts.loadState.append
    val dateFormater  = SimpleDateFormat("dd/MM/yyyy 'a las' HH:mm:ss")

    val state = rememberSwipeRefreshState(
        isRefreshing = pagingPosts.loadState.refresh is LoadState.Loading
    )

    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        state = state,
        // use the provided LazyPagingItems.refresh() method,
        // no need for custom solutions
        onRefresh = { pagingPosts.refresh() }
    ) {
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if (pagingPosts.loadState.refresh is LoadState.NotLoading) {
                items(items = pagingPosts){ post ->
                    post?.let{
                        val postId = it.postId?:""
                        val liked = it.liked?:false
                        var date = "10 de mayo del 2023, 10:23:11"
                        it.createdAt?.let { dateFirebase ->
                            date = dateFormater.format(dateFirebase.toDate())
                        }

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
                                onPostClick = {
                                    val params = OpenPostArgs(
                                        postId,
                                        it1.toList(),
                                        it.user?.name ?: "",
                                        it.user?.photo ?: "",
                                        it.titulo?: "",
                                        it.descripcion?:"",
                                        date,
                                        liked,
                                        it.bookmarked?:false,
                                        it.likes?.toInt() ?: 0
                                    )
                                    onPostClick(params)
                                }
                            )
                        }
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