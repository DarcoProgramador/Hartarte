package com.proyecpg.hartarte.ui.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.HeartBroken
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyecpg.hartarte.ui.theme.HartarteTheme

@Composable
fun SearchScreen(
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

                SearchBar(isSearchOpened)

                Spacer(modifier = Modifier.size(5.dp))

                Divider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
        }
    ) { innerPadding ->
        innerPadding
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
    isOpened: Boolean
){
    var isMostRecent by remember{ mutableStateOf(true) }
    var isMostLiked by remember{ mutableStateOf(false) }
    var isMostBookmarked by remember{ mutableStateOf(false) }

    AnimatedVisibility(
        visible = isOpened,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        LazyRow(content = {
            item {
                isMostRecent = filterButton(
                    isTrueChecked = isMostRecent,
                    pairIsTrue = Pair(Icons.Default.ArrowUpward, "Más recientes"),
                    pairIsNotTrue = Pair(Icons.Default.ArrowDownward, "Menos recientes")
                )

                Spacer(modifier = Modifier.width(10.dp))

                isMostLiked = filterButton(
                    isTrueChecked = isMostLiked,
                    pairIsTrue = Pair(Icons.Default.Favorite, "Más gustados"),
                    pairIsNotTrue = Pair(Icons.Default.HeartBroken, "Menos gustados")
                )

                Spacer(modifier = Modifier.width(10.dp))

                isMostBookmarked = filterButton(
                    isTrueChecked = isMostBookmarked,
                    pairIsTrue = Pair(Icons.Default.Bookmarks, "Más guardados"),
                    pairIsNotTrue = Pair(Icons.Default.Bookmark, "Menos guardados")
                )
            }
        })
    }
}

@Composable
fun filterButton(
    isTrueChecked: Boolean,
    pairIsTrue: Pair<ImageVector, String>,
    pairIsNotTrue: Pair<ImageVector, String>
): Boolean {

    var isTrue by remember{ mutableStateOf(isTrueChecked) }

    OutlinedButton(
        onClick = { isTrue = !isTrue },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Icon(
            imageVector = if (isTrue) pairIsTrue.first else pairIsNotTrue.first,
            contentDescription = "Chronological icon"
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(text = if (isTrue) pairIsTrue.second else pairIsNotTrue.second)
    }

    return isTrue
}

@Preview(showBackground = true)
@Composable
fun PreviewFilterButton(){
    HartarteTheme {
        Box(modifier = Modifier.padding(10.dp)){
            filterButton(
                isTrueChecked = true,
                pairIsTrue = Pair(
                    Icons.Outlined.Favorite,
                    "Más gustados"
                ),
                pairIsNotTrue = Pair(
                    Icons.Outlined.HeartBroken,
                    "Menos gustados"
                )
            )
        }
    }
}

@Preview
@Composable
fun PreviewSearchBar(){
    HartarteTheme {
        Box(modifier = Modifier.padding(all = 10.dp)){
            SearchBar(isOpened = true)
        }
    }
}

@Preview
@Composable
fun PreviewSearchScreen(){
    HartarteTheme {
        Box(modifier = Modifier.fillMaxSize()){
            SearchScreen(
                onReturn = {}
            )
        }
    }
}