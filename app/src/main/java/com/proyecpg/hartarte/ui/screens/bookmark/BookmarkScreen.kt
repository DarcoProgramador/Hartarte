package com.proyecpg.hartarte.ui.screens.bookmark

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.proyecpg.hartarte.ui.screens.post.open.OpenPostArgs
import com.proyecpg.hartarte.ui.theme.HartarteTheme
import java.text.SimpleDateFormat

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BookmarkScreen(
    paddingValues: PaddingValues,
    viewModel: BookmarkViewModel,
    onPostClick: (OpenPostArgs) -> Unit,
    onPostSharedProcess: (PostSharedEvent) -> Unit,
    stateLiked : HashMap<String, Boolean>,
    stateBookmarked : HashMap<String, Boolean>
){
    BookmarkScreenContent(innerPadding = paddingValues, viewModel = viewModel,
        onPostClick = onPostClick,
        onPostSharedProcess = onPostSharedProcess,
        stateLiked = stateLiked, stateBookmarked = stateBookmarked
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BookmarkScreenContent(
    innerPadding: PaddingValues,
    viewModel: BookmarkViewModel,
    onPostClick: (OpenPostArgs) -> Unit,
    onPostSharedProcess: (PostSharedEvent) -> Unit,
    stateLiked : HashMap<String, Boolean>,
    stateBookmarked : HashMap<String, Boolean>
) {
    val postBookmarkState = viewModel.postBookmarkState.collectAsStateWithLifecycle()

    if (postBookmarkState.value == null ){
        return
    }

    val pagingPosts = postBookmarkState.value!!.collectAsLazyPagingItems()
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
        onRefresh = { viewModel.updateBookmarkedPosts() }
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
                        var date = "10 de mayo del 2023, 10:23:11"
                        val username = it.user?.name ?: ""
                        val userPic =  it.user?.photo ?: ""
                        val title = it.titulo?:""
                        val description = it.descripcion?:""
                        val likeCount = it.likes?.toInt() ?: 0
                        val liked = stateLiked[postId]?:it.liked?:false
                        val bookmarked = stateBookmarked[postId]?:it.bookmarked?:false
                        it.createdAt?.let { dateFirebase ->
                            date = dateFormater.format(dateFirebase.toDate())
                        }

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
                                    viewModel.doLike(postId, like)
                                    onPostSharedProcess(PostSharedEvent.OnLiked(postId, like))
                                },
                                onBookmark = { postId : String, bookmark : Boolean ->
                                    viewModel.doBookmark(postId, bookmark)
                                    onPostSharedProcess(PostSharedEvent.OnBookmarked(postId, bookmark))
                                },
                                onPostClick = {
                                    onPostClick(OpenPostArgs(
                                        postId = postId,
                                        postImages = it1.toList(),
                                        postUsername = username,
                                        postUserPic = userPic,
                                        postTitle = title,
                                        postDescription = description,
                                        postDate = date,
                                        likesCount = likeCount,
                                        isBookmarked = bookmarked,
                                        isLiked = liked
                                    )
                                    )
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
@Preview
@Composable
fun PreviewBookmarkScreen(){
    HartarteTheme{
        Box(modifier = Modifier.fillMaxSize()){
            BookmarkScreen(
                paddingValues = PaddingValues(),
                viewModel = hiltViewModel(),
                onPostClick = {},
                onPostSharedProcess = {},
                stateLiked = hashMapOf(),
                stateBookmarked = hashMapOf()
            )
        }
    }
}