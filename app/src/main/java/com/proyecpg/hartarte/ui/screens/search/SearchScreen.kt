package com.proyecpg.hartarte.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.android.paging3.flow
import com.algolia.instantsearch.compose.filter.facet.FacetListState
import com.algolia.instantsearch.compose.item.StatsState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.search.model.search.Facet
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.proyecpg.hartarte.data.model.PostSerial
import com.proyecpg.hartarte.ui.components.ErrorItem
import com.proyecpg.hartarte.ui.components.LoadingItem
import com.proyecpg.hartarte.ui.components.SearchBar
import com.proyecpg.hartarte.ui.components.SearchingPost
import com.proyecpg.hartarte.ui.screens.PostSharedEvent
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    searchBoxState: SearchBoxState,
    paginator: Paginator<PostSerial>,
    statsText: StatsState<String>,
    onPostClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    onPostSharedProcess: (PostSharedEvent) -> Unit,
    onReturn: () -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val pagingHits = paginator.flow.collectAsLazyPagingItems()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        topBar = {
            Column {
                SearchTopBar( onReturn = onReturn )

                SearchBar(
                    searchBoxState = searchBoxState,
                    pagingHits = pagingHits,
                    listState = listState,
                    onValueChange = { scope.launch { listState.scrollToItem(0) } }
                )

                if (searchBoxState.query.isNotEmpty()) {
                    Stats(stats = statsText.stats)
                }

                Divider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }

    ) { innerPadding ->
        SearchScreenContent(innerPadding = innerPadding, paginator = paginator, onPostClick = onPostClick, onUserClick = onUserClick
        )
    }
}

@Composable
fun Stats(stats: String) {
    Text(
        modifier = Modifier.padding(start = 12.dp),
        text = stats,
        fontSize = 16.sp,
        maxLines = 1
    )
}

@Composable
fun FacetRow(
    modifier: Modifier = Modifier,
    selectableFacet: SelectableItem<Facet>
) {
    val (facet, isSelected) = selectableFacet
    Row(
        modifier = modifier.height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(1f)) {
            Text(
                modifier = Modifier.alignByBaseline(),
                text = facet.value,
                fontSize = 16.sp
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .alignByBaseline(),
                text = facet.count.toString(),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
            )
        }
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
            )
        }
    }
}

@Composable
fun FacetList(
    modifier: Modifier = Modifier,
    facetList: FacetListState
) {
    Column(modifier) {
        Text(
            text = "Categories",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(14.dp)
        )
        LazyColumn(Modifier.background(MaterialTheme.colorScheme.background)) {
            items(facetList.items) { item ->
                FacetRow(
                    modifier = Modifier
                        .clickable { facetList.onSelection?.invoke(item.first) }
                        .padding(horizontal = 14.dp),
                    selectableFacet = item,
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .width(1.dp)
                )
            }
        }
    }
}

@Composable
fun SearchScreenContent(
    innerPadding: PaddingValues,
    paginator: Paginator<PostSerial>,
    onPostClick: (String) -> Unit,
    onUserClick: (String) -> Unit
){
    //Posts
    val postSearchState = paginator.flow.collectAsStateWithLifecycle(initialValue = 0)

    if (postSearchState.value == null ){
        return
    }

    val pagingPosts = paginator.flow.collectAsLazyPagingItems()
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
                        val postId = it.objectID.toString()
                        val username = it.user.name
                        val userPic = it.user.photo
                        val title = it.titulo
                        val description = it.descripcion

                        it.images?.let { it1 ->
                            SearchingPost(
                                images = it1.toList(),
                                username = username,
                                userPic = userPic,
                                title = title,
                                description = description,
                                onPostClick = {
                                    onPostClick(postId)
                                },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar( onReturn: () -> Unit ){

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
        }
    )
}