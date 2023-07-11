package com.proyecpg.hartarte.ui.screens.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.proyecpg.hartarte.ui.theme.HartarteTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun UserScreen(
    paddingValues: PaddingValues,
    onProcessUSer : (UserEvent) -> Unit,
    userEditState : UserState,
    userState : UserUI,
    postUser : Flow<PagingData<Post>>,
    onPostClick: (String) -> Unit,
    onPostSharedProcess: (PostSharedEvent) -> Unit,
    stateLiked : HashMap<String, Boolean>,
    stateBookmarked : HashMap<String, Boolean>
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

@Preview
@Composable
fun PreviewUserScreen(){
    HartarteTheme {
        val emptyPost = flowOf(PagingData.empty<Post>())
        Box(modifier = Modifier.padding(all = 10.dp)){
            UserScreen(
                paddingValues = PaddingValues(),
                onProcessUSer = {},
                userEditState = UserState(),
                userState = UserUI(username = "Prueba", descripcion = "descipcion"),
                onPostClick = {},
                postUser = emptyPost,
                onPostSharedProcess = {},
                stateBookmarked = hashMapOf(),
                stateLiked = hashMapOf()
            )
        }
    }
}