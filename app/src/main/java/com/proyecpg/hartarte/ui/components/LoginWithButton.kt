package com.proyecpg.hartarte.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecpg.hartarte.R

@Composable
fun GoogleLoginButton(
    onClick : () -> Unit
){
    Box(
        modifier = Modifier.height(IntrinsicSize.Min),
        contentAlignment = Alignment.Center
    ){
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .shadow(5.dp, shape = RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            onClick = onClick

        ) {
            Image(
                painter = painterResource(id = R.drawable.img_google),
                contentDescription = stringResource(id = R.string.google_logo)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = stringResource(id = R.string.google_login),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
fun FacebookLoginButton(
    //OnClick
){

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .shadow(5.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        onClick = {
            /* TODO: Llamar a la respectiva función OnClick */
        }
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_facebook),
            contentDescription = stringResource(id = R.string.facebook_logo)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = stringResource(id = R.string.facebool_login),
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}