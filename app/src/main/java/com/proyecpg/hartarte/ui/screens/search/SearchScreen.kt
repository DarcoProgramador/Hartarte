package com.proyecpg.hartarte.ui.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.android.paging3.flow
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.proyecpg.hartarte.data.product.Product
import com.proyecpg.hartarte.ui.components.ErrorItem
import com.proyecpg.hartarte.ui.components.LoadingItem
import com.proyecpg.hartarte.ui.components.Post
import com.proyecpg.hartarte.ui.components.SearchBar
import com.proyecpg.hartarte.ui.screens.PostSharedEvent
import com.proyecpg.hartarte.ui.screens.post.open.OpenPostArgs
import com.proyecpg.hartarte.ui.theme.HartarteTheme
import com.proyecpg.hartarte.utils.QueryParams
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@Composable
fun ProductsList(
    modifier: Modifier = Modifier,
    pagingHits: LazyPagingItems<Product>,
    listState: LazyListState
) {
    LazyColumn(modifier, listState) {
        items(pagingHits) { item ->
            if (item == null) return@items
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                text = item.name,
                fontSize = 16.sp
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(1.dp)
            )
        }
    }
}

@Composable
fun Search(
    modifier: Modifier = Modifier,
    searchBoxState: SearchBoxState,
    paginator: Paginator<Product>
) {
    val scope = rememberCoroutineScope()
    val pagingHits = paginator.flow.collectAsLazyPagingItems()
    val listState = rememberLazyListState()

    Column(modifier) {
        SearchBox(
            searchBoxState = searchBoxState,
            onValueChange = { scope.launch { listState.scrollToItem(0) } },
        )
        ProductsList(
            modifier = Modifier.fillMaxSize(),
            pagingHits = pagingHits,
            listState = listState,
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBox(
    searchBoxState: SearchBoxState = SearchBoxState(),
    onValueChange: (String) -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, start = 12.dp),
        shape = RoundedCornerShape(30.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            focusedIndicatorColor = MaterialTheme.colorScheme.background,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
            errorIndicatorColor = MaterialTheme.colorScheme.background,
        ),
        value = searchBoxState.query,
        // Update text on value change
        onValueChange = {
            searchBoxState.setText(it)
            onValueChange(it)
        },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 16.sp
        ),
        placeholder = {
            Text(
                text = "Buscar..."
            )
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        },
        trailingIcon = {
            if(searchBoxState.query.isNotEmpty()){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close search bar",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .clickable {
                        searchBoxState.setText("")
                        onValueChange("")
                    }
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        // Set text as query submit on search action
        keyboardActions = KeyboardActions(
            onSearch = {
                searchBoxState.setText(searchBoxState.query, true)
                keyboardController?.hide()
            }
        ),
        maxLines = 1
    )
}

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onPostClick: (OpenPostArgs) -> Unit,
    onPostSharedProcess: (PostSharedEvent) -> Unit,
    stateLiked : HashMap<String, Boolean>,
    stateBookmarked : HashMap<String, Boolean>,
    onReturn: () -> Unit
){
    var isSearchOpened by remember{ mutableStateOf(false) }

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

                SearchBar()

                Spacer(modifier = Modifier.size(5.dp))

                SearchFilters(viewModel, isSearchOpened)



                Divider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
        }
    ) { innerPadding ->
        SearchScreenContent(innerPadding = innerPadding, viewModel = viewModel,
            onPostClick = onPostClick, onPostSharedProcess = onPostSharedProcess,
            stateLiked = stateLiked, stateBookmarked = stateBookmarked
        )
    }
}

@Composable
fun SearchScreenContent(
    innerPadding: PaddingValues,
    viewModel: SearchViewModel,
    onPostClick: (OpenPostArgs) -> Unit,
    onPostSharedProcess: (PostSharedEvent) -> Unit,
    stateLiked : HashMap<String, Boolean>,
    stateBookmarked : HashMap<String, Boolean>
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
                        imageVector = Icons.Default.FilterAlt,
                        contentDescription = "Filters icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    )
}


@Composable
fun SearchFilters(
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
fun PreviewSearchFilters(){
    HartarteTheme {
        Box(modifier = Modifier.padding(all = 10.dp)){
            SearchFilters(
                viewModel = hiltViewModel(),
                isOpened = true
            )
        }
    }
}