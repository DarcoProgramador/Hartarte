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
import com.proyecpg.hartarte.ui.screens.PostSharedEvent

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    viewModel: HomeViewModel,
    onPostClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    onPostSharedProcess: (PostSharedEvent) -> Unit,
    stateLiked : HashMap<String, Boolean>,
    onImageClick: (Array<String>) -> Unit,
    stateBookmarked : HashMap<String, Boolean>
) {
    HomeScreenContent(
        innerPadding = paddingValues,
        viewModel = viewModel,
        onPostClick = onPostClick,
        onPostSharedProcess = onPostSharedProcess,
        stateLiked = stateLiked,
        stateBookmarked = stateBookmarked,
<<<<<<< HEAD
        onImageClick = onImageClick,
=======
>>>>>>> 830332a (ViewModel and onClick events)
        onUserClick = onUserClick
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreenContent(
    innerPadding: PaddingValues,
    viewModel: HomeViewModel,
    onPostClick: (String) -> Unit,
<<<<<<< HEAD
    onImageClick: (Array<String>) -> Unit,
=======
>>>>>>> 830332a (ViewModel and onClick events)
    onUserClick: (String) -> Unit,
    onPostSharedProcess: (PostSharedEvent) -> Unit,
    stateLiked : HashMap<String, Boolean>,
    stateBookmarked : HashMap<String, Boolean>
){
    //Posts
    val pagingPosts = viewModel.posts.collectAsLazyPagingItems()
    val refresh = pagingPosts.loadState.refresh
    val append = pagingPosts.loadState.append

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
                        val username = it.user?.name ?: ""
                        val userPic =  it.user?.photo ?: ""
                        val title = it.titulo?:""
                        val description = it.descripcion?:""
                        val likeCount = it.likes?.toInt() ?: 0
                        val liked = stateLiked[postId]?:it.liked?:false
                        val bookmarked = stateBookmarked[postId]?:it.bookmarked?:false

                        it.images?.let { it1 ->
                            Post(
                                postId = postId,
                                images = it1.toList(),
                                username = username,
                                userPic = userPic,
                                title = title,
                                description = description,
                                isLiked = liked,
                                isBookmarked = bookmarked,
                                likesCount = likeCount,
                                onLike = { postId : String, like : Boolean ->
                                    onPostSharedProcess(PostSharedEvent.OnLiked(postId, like))
                                },
                                onBookmark ={ postId : String, bookmark : Boolean ->
                                    onPostSharedProcess(PostSharedEvent.OnBookmarked(postId, bookmark))
                                } ,
                                onPostClick = {
                                    onPostClick(postId)
                                },
<<<<<<< HEAD
                                onImageClick = {
                                    var arrayImages = it1.toTypedArray()
                                    onImageClick(arrayImages)
                                },
=======
>>>>>>> 830332a (ViewModel and onClick events)
                                onUserClick = {
                                    onUserClick(it.user!!.uid!!)
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
        onPostClick = {},
        onPostSharedProcess = { _:PostSharedEvent -> run{  }},
        stateBookmarked = hashMapOf(),
        stateLiked = hashMapOf(),
<<<<<<< HEAD
        onImageClick = {},
=======
>>>>>>> 830332a (ViewModel and onClick events)
        onUserClick = {}
    )
}