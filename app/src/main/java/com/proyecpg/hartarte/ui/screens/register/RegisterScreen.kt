package com.proyecpg.hartarte.ui.screens.register

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecpg.hartarte.R
import com.proyecpg.hartarte.ui.components.customPasswordField
import com.proyecpg.hartarte.ui.components.customTextField
import com.proyecpg.hartarte.ui.components.ProgressButton

@Composable
fun RegisterScreen(
    state: RegisterState
    //OnLoginClick
    //OnRegisterClick
){

    var username: String
    var email: String
    var password: String
    var passwordConfirmation: String

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp, vertical = 40.dp)
    ) {
        item {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = stringResource(id = R.string.back)
                )
            }

            Spacer(modifier = Modifier.size(20.dp))

            Text(
                text = stringResource(id = R.string.register),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            Spacer(modifier = Modifier.size(40.dp))

            TitleText( stringResource(id = R.string.title_user) )

            username = customTextField( stringResource(id = R.string.username))

            Spacer(modifier = Modifier.size(30.dp))

            TitleText( stringResource(id = R.string.title_email) )

            email = customTextField( stringResource(id = R.string.email) )

            Spacer(modifier = Modifier.size(30.dp))

            TitleText( stringResource(id = R.string.title_password) )

            password = customPasswordField( stringResource(id = R.string.password) )

            Spacer(modifier = Modifier.size(30.dp))

            TitleText( stringResource(id = R.string.title_password_confirm) )

            passwordConfirmation = customPasswordField( stringResource(id = R.string.password_confirm) )

            Spacer(modifier = Modifier.size(40.dp))

            ProgressButton( stringResource(id = R.string.register), state.isLoading , onEventClick = { TODO("Añadir evento para registrar") })
        }
    }
}

@Composable
fun TitleText(text: String){
    Text(
        text = text,
        style = TextStyle(
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier.padding(bottom = 7.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen(){
    RegisterScreen(state = RegisterState(isLoading = false))
}