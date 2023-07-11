package com.proyecpg.hartarte.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.proyecpg.hartarte.data.product.Product

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    searchBoxState: SearchBoxState = SearchBoxState(),
    pagingHits: LazyPagingItems<Product>,
    listState: LazyListState,
    onValueChange: (String) -> Unit = {}
){
    var active by remember{ mutableStateOf(false) }
    val items = remember { mutableListOf<String>() }
    val keyboardController = LocalSoftwareKeyboardController.current

    androidx.compose.material3.SearchBar(
        query = searchBoxState.query,
        onQueryChange = {
            searchBoxState.setText(it)
            onValueChange(it)
        },
        onSearch = {
            searchBoxState.setText(searchBoxState.query, true)
            keyboardController?.hide()
            items.add(searchBoxState.query)
            active = false
        },
        active = active,
        onActiveChange = {
            active = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(30.dp)),
        placeholder = {
            Text(text = "Buscar publicación")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        trailingIcon = {
            if(active){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close search bar",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.clickable {
                        if (searchBoxState.query.isNotEmpty()){
                            searchBoxState.setText("")
                            onValueChange("")
                        }
                        else{
                            active = false
                        }
                    }
                )
            }
        }
    ) {
        items.reversed().forEach {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        searchBoxState.setText(it)
                    },
                verticalAlignment = CenterVertically
            ){
                Icon(imageVector = Icons.Default.History, contentDescription = "History icon")

                Spacer(modifier = Modifier.width(10.dp))

                Text(text = it)
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = listState
        ){
            items(pagingHits) {item ->
                if (item == null) return@items
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable {
                            searchBoxState.setText(item.name)
                        },
                    verticalAlignment = CenterVertically
                ){
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(text = item.name)
                }
            }
        }
    }
}