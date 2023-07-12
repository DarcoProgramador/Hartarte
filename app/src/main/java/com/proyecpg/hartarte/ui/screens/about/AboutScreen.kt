package com.proyecpg.hartarte.ui.screens.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.proyecpg.hartarte.R
import com.proyecpg.hartarte.ui.screens.user.isScrolled
import kotlinx.coroutines.launch

@Preview
@Composable
fun AboutScreen()
{
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.app))
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        LottieAnimation(modifier = Modifier.size(400.dp),composition = composition)
         Card(  modifier = Modifier
             .fillMaxWidth(),
             colors = CardDefaults.cardColors(
                 containerColor = MaterialTheme.colorScheme.primaryContainer
             )){
             Text(text = "Conoce HARTARTE",
                 modifier = Modifier.padding(50.dp),
                 textAlign = TextAlign.Center,
             )

         }

    }
}

