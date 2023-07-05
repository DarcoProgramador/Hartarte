package com.proyecpg.hartarte.ui.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.proyecpg.hartarte.ui.components.ErrorItem
import com.proyecpg.hartarte.ui.components.LoadingItem
import com.proyecpg.hartarte.ui.components.Post
import com.proyecpg.hartarte.ui.screens.post.open.OpenPostArgs
import com.proyecpg.hartarte.ui.theme.HartarteTheme
import com.proyecpg.hartarte.utils.QueryParams
import java.text.SimpleDateFormat

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onPostClick: (OpenPostArgs) -> Unit,
    onReturn: () -> Unit
){
    var isSearchOpened by remember{ mutableStateOf(true) }

    Scaffold(
        modifier = Modifier
            .padding(all = 12.dp),
        topBar = {
            Column {
                TopBar(
                    isOpened = isSearchOpened,
                    onSearchClick = {
                        isSearchOpened = !isSearchOpened
                    },
                    onReturn = onReturn
                )

                SearchBar(viewModel, isSearchOpened)

                Spacer(modifier = Modifier.size(5.dp))

                Divider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
        }
    ) { innerPadding ->
        SearchScreenContent(innerPadding = innerPadding, viewModel = viewModel, onPostClick = onPostClick)
    }
}

@Composable
fun SearchScreenContent(
    innerPadding: PaddingValues,
    viewModel: SearchViewModel,
    onPostClick: (OpenPostArgs) -> Unit
){
    //Posts
    val postSearchState = viewModel.postSearchState.collectAsStateWithLifecycle()

    if (postSearchState.value == null ){
        return
    }

    val pagingPosts = postSearchState.value!!.collectAsLazyPagingItems()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    isOpened: Boolean,
    onSearchClick: () -> Unit,
    onReturn: () -> Unit
){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Buscar",
                color = MaterialTheme.colorScheme.primary
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onReturn
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Return icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        actions = {
            IconButton(
                onClick = onSearchClick
            ) {
                if (isOpened){
                    Icon(
                        imageVector = Icons.Default.ExpandLess,
                        contentDescription = "Expand less icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                else {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    )
}


@Composable
fun SearchBar(
    viewModel: SearchViewModel,
    isOpened: Boolean
) {
    var isChecked by remember{ mutableStateOf("") }

    val icons = listOf(
        Icons.Default.ArrowUpward to "Más recientes",
        Icons.Default.Favorite to "Más gustados",
        Icons.Default.Bookmarks to "Más guardados"
    )

    AnimatedVisibility(
        visible = isOpened,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            item {
                for ((icon, description) in icons){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        OutlinedIconToggleButton(
                            checked = isChecked == description,
                            onCheckedChange = {
                                isChecked = description

                                viewModel.onQueryChange(
                                    when (isChecked) {
                                        "Más recientes" -> QueryParams.MOST_RECENT
                                        "Más gustados" -> QueryParams.MOST_LIKED
                                        "Más guardados" -> QueryParams.MOST_BOOKMARKED
                                        else -> null
                                    }
                                )
                            }
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = description
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(text = description)
                    }

                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewSearchBar(){
    HartarteTheme {
        Box(modifier = Modifier.padding(all = 10.dp)){
            SearchBar(
                viewModel = hiltViewModel(),
                isOpened = true
            )
        }
    }
}