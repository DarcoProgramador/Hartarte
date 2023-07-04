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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.proyecpg.hartarte.domain.model.Post
import com.proyecpg.hartarte.ui.components.Post
import com.proyecpg.hartarte.ui.model.UserUI
import com.proyecpg.hartarte.ui.screens.home.ErrorItem
import com.proyecpg.hartarte.ui.screens.home.LoadingItem
import com.proyecpg.hartarte.ui.screens.post.open.OpenPostArgs
import com.proyecpg.hartarte.ui.theme.HartarteTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.text.SimpleDateFormat

@Composable
fun UserScreen(
    paddingValues: PaddingValues,
    onProcessUSer : (UserEvent) -> Unit,
    userEditState : UserState,
    userState : UserUI,
    postUser : Flow<PagingData<Post>>,
    onPostClick: (OpenPostArgs) -> Unit
){
    val lazyListState = rememberLazyListState()
    val dateFormater  = SimpleDateFormat("dd/MM/yyyy 'a las' HH:mm:ss")

    //Posts
    val pagingPosts = postUser.collectAsLazyPagingItems()
    val refresh = pagingPosts.loadState.refresh
    val append = pagingPosts.loadState.append

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

        LazyColumn(
            modifier = Modifier,
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
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
                            onLike = {id : String, like: Boolean ->
                                onProcessUSer(UserEvent.UserPostLikeClicked(
                                    postId = id,
                                    liked = like
                                ))},
                            onBookmark = {id : String, bookmark: Boolean ->
                                onProcessUSer(UserEvent.UserPostBookmarkCliked(
                                    postId = id,
                                    bookmarked = bookmark
                                ))},
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
                postUser = emptyPost
            )
        }
    }
}