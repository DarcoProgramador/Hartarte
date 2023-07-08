package com.proyecpg.hartarte.ui.screens.post.open

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.proyecpg.hartarte.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OpenPostImageScreen(
    imagen: String
) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                //.data(postImages[page])
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .crossfade(true)
                .scale(Scale.FILL)
                .build(),
            contentDescription = "Carousel image",
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
    }

}
