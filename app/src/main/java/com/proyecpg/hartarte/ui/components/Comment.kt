package com.proyecpg.hartarte.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.proyecpg.hartarte.ui.theme.HartarteTheme

@Composable
fun Comment(
    image: String,
    username: String,
    description: String,
    date: String,
    onPostUserClick: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 150.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                border = null,
            ) {
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable {
                            onPostUserClick()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    AsyncImage(
                        model = image,
                        contentDescription = "User avatar",
                        modifier = Modifier
                            .size(35.dp)
                            .clip(shape = CircleShape)
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    Text(
                        text = username
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp)
                ){
                    item {
                        Text(
                            text = description,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 3.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = date,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewComment(){
    HartarteTheme {
        Box(modifier = Modifier
            .fillMaxWidth()
        ){
            Comment(
                image = "https://cdn.discordapp.com/attachments/1029844385237569616/1116569644745097320/393368.png",
                username = "User",
                description = "Come comen comen comeno comeno comenio coo comeno comenta coio comeo comentar comenrio comtario mentario  comen comen comeno comeno comenta coio comeo comentar comenrio comtario.",
                date = "11 de mayo del 2020, 11:30 a.m.",
                onPostUserClick = {}
            )
        }
    }

}