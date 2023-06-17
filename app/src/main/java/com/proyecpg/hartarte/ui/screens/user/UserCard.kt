package com.proyecpg.hartarte.ui.screens.user

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.proyecpg.hartarte.R
import com.proyecpg.hartarte.ui.theme.HartarteTheme

@Composable
fun UserCard(
    image: String,
    username: String,
    userDescription: String,
    userFollowers: Int,
    userFollows: Int,
    isFollowed: Boolean,
    lazyListState: LazyListState
){
    var followed by remember { mutableStateOf(isFollowed) }
    val animatedSize: Dp by animateDpAsState(targetValue = if (lazyListState.isScrolled) 200.dp else 160.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (lazyListState.isScrolled) IntrinsicSize.Max else IntrinsicSize.Min),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            AsyncImage(
                model = image,
                contentDescription = "User image",
                modifier = Modifier
                    .size(animatedSize)
                    .clip(CircleShape)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = username,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(30.dp))

                if(lazyListState.isScrolled){
                    //Followers
                    Text(
                        text = "Seguidores",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Text(
                        text = userFollowers.toString(),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    //Follows
                    Text(
                        text = "Seguidos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Text(
                        text = userFollows.toString(),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                else{
                    Button(
                        onClick = {
                            followed = !followed
                            //Actualizar seguidores
                        },
                        modifier = Modifier.height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            painter = if (followed) painterResource(id = R.drawable.ic_favorite) else painterResource(id = R.drawable.ic_favorite_border),
                            contentDescription = "Follow button"
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(text = if(followed) "Dejar de seguir" else "Seguir" )
                    }
                }
            }
        }

        if(lazyListState.isScrolled){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = userDescription,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Button(
                    onClick = {
                        followed = !followed
                        //Actualizar seguidores
                    },
                    modifier = Modifier
                        .padding(start = 50.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        painter = if (followed) painterResource(id = R.drawable.ic_favorite) else painterResource(id = R.drawable.ic_favorite_border),
                        contentDescription = "Follow button"
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(text = if(followed) "Dejar de seguir" else "Seguir" )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewUserCard(){
    HartarteTheme {
        Box(modifier = Modifier.padding(all = 10.dp)){
            UserCard(
                image = "https://cdn.discordapp.com/attachments/1029844385237569616/1116569644745097320/393368.png",
                username = "Username",
                userDescription = "Esta descripción tiene activado un ellipsis y un límite de 3 líneas para la descripción con el fin de que no se vea muy largo todo.",
                userFollowers = 100,
                userFollows = 100,
                isFollowed = false,
                lazyListState = LazyListState(0, 0)
            )
        }
    }
}