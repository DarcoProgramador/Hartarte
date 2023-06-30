package com.proyecpg.hartarte.ui.screens.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyecpg.hartarte.ui.components.SearchBar
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