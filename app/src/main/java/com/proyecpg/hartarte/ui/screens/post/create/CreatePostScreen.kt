package com.proyecpg.hartarte.ui.screens.post.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.proyecpg.hartarte.ui.theme.HartarteTheme

@Composable
fun CreatePostScreen(){

}

@Preview
@Composable
fun PreviewCreatePostScreen(){
    HartarteTheme {
        Box(modifier = Modifier.fillMaxSize()){
            CreatePostScreen()
        }
    }
}