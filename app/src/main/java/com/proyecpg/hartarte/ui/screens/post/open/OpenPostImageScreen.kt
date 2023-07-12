package com.proyecpg.hartarte.ui.screens.post.open

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.proyecpg.hartarte.utils.Constants

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OpenPostImageScreen(imagen:List<String>) {
    for (i in imagen.indices!!) {
        Log.e(Constants.TAG, imagen[i])
    }

    val pagerState = rememberPagerState(initialPage = 0)
  //  val scale = remember { mutableStateOf(1f) }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(Color.Gray)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, rotation ->
                   // scale.value *= zoom
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
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffset
            val imageSize by animateFloatAsState(
                targetValue = if ( pageOffset != 0.0f) 0.75f else 1f,
                animationSpec = tween(durationMillis = 300)
            )
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imagen[page])
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .build(),
                contentDescription = "Carousel image",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = imageSize
                        scaleY = imageSize
                    }
                   /* .graphicsLayer(
                        scaleX = maxOf(.3f, minOf(2f, scale.value)),
                        scaleY = maxOf(.3f, minOf(2f, scale.value)),
                    )*/
                    .clip(RoundedCornerShape(0.dp)),
                contentScale = ContentScale.FillWidth
            )
        }
    }

}

