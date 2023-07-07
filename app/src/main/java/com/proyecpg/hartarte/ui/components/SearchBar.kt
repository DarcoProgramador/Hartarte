package com.proyecpg.hartarte.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyecpg.hartarte.ui.theme.HartarteTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(){
    var text by remember{ mutableStateOf("") }
    var active by remember{ mutableStateOf(false) }
    val items = remember { mutableListOf<String>() }

    androidx.compose.material3.SearchBar(
        query = text,
        onQueryChange = {
            text = it
        },
        onSearch = {
            val request = text.trim()

            if (request.isNotEmpty()){
                if (!items.contains(request)){
                    items.add(request)
                }
                active = false

                /* TODO: Buscar */
            }
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
                        if (text.isNotEmpty()){
                            text = ""
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
                        text = it
                    },
                verticalAlignment = CenterVertically
            ){
                Icon(imageVector = Icons.Default.History, contentDescription = "History icon")

                Spacer(modifier = Modifier.width(10.dp))

                Text(text = it)
            }
        }
    }
}

@Preview
@Composable
fun PreviewSearchBar(){
    HartarteTheme {
        Box(modifier = Modifier.padding(all = 10.dp)){
            SearchBar()
        }
    }
}