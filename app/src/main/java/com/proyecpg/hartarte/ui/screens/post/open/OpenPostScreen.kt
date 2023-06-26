package com.proyecpg.hartarte.ui.screens.post.open

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.proyecpg.hartarte.ui.theme.HartarteTheme

@Composable
fun OpenPostScreen(){
    Scaffold(
        modifier = Modifier,
        topBar = {

        }
    ) { innerPadding ->
        innerPadding
    }
}

@Preview
@Composable
fun PreviewOpenPostScreen(){
    HartarteTheme {
        Box(modifier = Modifier.fillMaxSize()){
            OpenPostScreen()
        }
    }
}