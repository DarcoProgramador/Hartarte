package com.proyecpg.hartarte.ui.screens.user.open

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.proyecpg.hartarte.domain.model.Post
import com.proyecpg.hartarte.ui.components.ErrorItem
import com.proyecpg.hartarte.ui.components.LoadingItem
import com.proyecpg.hartarte.ui.components.Post
import com.proyecpg.hartarte.ui.model.UserUI
import com.proyecpg.hartarte.ui.screens.PostSharedEvent
import com.proyecpg.hartarte.ui.screens.user.NonUserCard
import kotlinx.coroutines.flow.Flow

@Composable
fun OpenUserScreen(
    userState: UserUI,
    postUser : Flow<PagingData<Post>>,
    stateLiked : HashMap<String, Boolean>,
    stateBookmarked : HashMap<String, Boolean>,
    onPostClick: (String) -> Unit,
    onPostSharedProcess: (PostSharedEvent) -> Unit,
    onReturn: () -> Unit
){
    Scaffold(
        modifier = Modifier,
        topBar = {
            OpenUserTopAppBar(
                userName = userState.username?:"Username",
                onClick = onReturn
            )
        }
    ) { innerPadding ->
        OpenUserScreenContent(
            paddingValues = innerPadding, userState = userState, postUser = postUser,
            stateLiked = stateLiked, stateBookmarked = stateBookmarked,
<<<<<<< HEAD
            onProcessUser = onProcessUser,
=======
>>>>>>> 830332a (ViewModel and onClick events)
            onPostClick = onPostClick, onPostSharedProcess = onPostSharedProcess
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenUserTopAppBar(
    userName: String,
    onClick: () -> Unit
){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = userName,
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

@Composable
fun OpenUserScreenContent(
    paddingValues: PaddingValues,
    userState: UserUI,
    postUser : Flow<PagingData<Post>>,
    stateLiked : HashMap<String, Boolean>,
    stateBookmarked : HashMap<String, Boolean>,
    onPostClick: (String) -> Unit,
    onPostSharedProcess: (PostSharedEvent) -> Unit
){
    val lazyListState = rememberLazyListState()

    //Posts
    val pagingPosts = postUser.collectAsLazyPagingItems()
    val refresh = pagingPosts.loadState.refresh
    val append = pagingPosts.loadState.append

    val state = rememberSwipeRefreshState(
        isRefreshing = pagingPosts.loadState.refresh is LoadState.Loading
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ){
        Box(modifier = Modifier){
            NonUserCard(
                userImage = userState.photo,
                username = userState.username?:"User",
                userDescription = userState.descripcion,
                lazyListState = lazyListState
            )
        }

        Divider(
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(vertical = 10.dp)
        )
        SwipeRefresh(
            modifier = Modifier.fillMaxSize(),
            state = state,
            // use the provided LazyPagingItems.refresh() method,
            // no need for custom solutions
            onRefresh = { pagingPosts.refresh() }
        ) {
            LazyColumn(
                modifier = Modifier,
                state = lazyListState,
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
                                    onBookmark = { postId : String, bookmark : Boolean ->
                                        onPostSharedProcess(PostSharedEvent.OnBookmarked(postId, bookmark))
                                    },
                                    onUserClick = {},
                                    onPostClick = {
                                        onPostClick(postId)
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
}

//Revisa si está scrolleado para colapasar un elemento
val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 3 || firstVisibleItemScrollOffset > 3

@Preview(showBackground = true)
@Composable
fun PreviewOpenUserScreen(){

}