package com.proyecpg.hartarte.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecpg.hartarte.R
import com.proyecpg.hartarte.ui.theme.HartarteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Post(
    //Image
    title: String,
    description: String,
    likesCount: Int,
    isLiked: Boolean,
    isBookmarked: Boolean
){
    val interactionSource = remember { MutableInteractionSource() }

    /* TODO: Recibir parámetros */
    var isLiked by remember { mutableStateOf(isLiked) }
    var isBookmarked by remember { mutableStateOf(isBookmarked) }
    var likesCount by remember { mutableStateOf(likesCount) }

    Card(
        onClick = {
                  /* TODO: Navegar al post */
        },
        modifier = Modifier.fillMaxWidth(0.94f),
        enabled = true,
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        interactionSource = interactionSource
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .height(intrinsicSize = IntrinsicSize.Min)
        ) {
            Image(
                /* TODO: Recibir URL en lugar de un painter */
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(color = Color.White),
                painter = painterResource(id = R.drawable.img_logo),
                contentDescription = "Post image"
            )

            Spacer(modifier = Modifier.size(8.dp))

            Column(
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                Text(
                    text = title,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = description,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 16.sp,
                    maxLines = 3
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    
                    Spacer(modifier = Modifier.size(10.dp))
                    
                    IconButton(
                        onClick = {
                            /* TODO: Actualizar likes y estado de like del usuario */
                            if (isLiked)
                                likesCount--
                            else
                                likesCount++

                            isLiked = !isLiked
                        }
                    ) {
                        if (isLiked){
                            Icon(
                                painter = painterResource(id = R.drawable.ic_favorite_filled),
                                contentDescription = "Like"
                            )
                        }
                        else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_favorite_outlined),
                                contentDescription = "Like"
                            )
                        }
                    }

                    Text(
                        text = likesCount.toString()
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 20.dp)
                    ) {
                        IconButton(
                            onClick = {
                                /* TODO: Actualizar bookmark */
                                isBookmarked = !isBookmarked
                            }
                        ) {
                            if(isBookmarked){
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_bookmark_filled),
                                    contentDescription = "Delete"
                                )
                            }
                            else{
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_bookmark_outlined),
                                    contentDescription = "Bookmark"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewPost(){
    HartarteTheme {
        Box(contentAlignment = Alignment.CenterStart) {
            Post(
                title = "Quesillo de turca",
                description = "Quesillo de turca, hecho en mi Masayita querida <3.\n #CocinaMasayense #OrgulloMasaya\n#OrgulloGa...",
                isBookmarked = true,
                isLiked = false,
                likesCount = 0
            )
        }
    }
}