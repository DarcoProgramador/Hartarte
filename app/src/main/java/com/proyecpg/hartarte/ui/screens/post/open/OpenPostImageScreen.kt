package com.proyecpg.hartarte.ui.screens.post.open

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.proyecpg.hartarte.R

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OpenPostImageScreen(
    imagen: List<String>
) {
    val pagerState = rememberPagerState(initialPage = 0)
    val scale = remember { mutableStateOf(1f) }
    val rotationState = remember { mutableStateOf(1f) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(Color.Gray)
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, rotation ->
                    scale.value *= zoom
                    rotationState.value += rotation
                }
            }
    ) {
        HorizontalPager(
            count = imagen.size,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black))
                ),
            state = pagerState,
            verticalAlignment = Alignment.CenterVertically
        ) { page ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    //.data(imagen[page])
                    .data(R.drawable.img_facebook)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .crossfade(true)
                    .build(),
                contentDescription = "Carousel image",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
                    .graphicsLayer(
                        scaleX = maxOf(.3f, minOf(2f, scale.value)),
                        scaleY = maxOf(.3f, minOf(2f, scale.value)),
                        rotationZ = rotationState.value
                    )
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.None
            )
        }
    }

}

