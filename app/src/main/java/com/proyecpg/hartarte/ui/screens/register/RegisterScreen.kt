package com.proyecpg.hartarte.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.proyecpg.hartarte.ui.components.EventDialog
import com.proyecpg.hartarte.ui.components.ProgressButton
import com.proyecpg.hartarte.ui.components.customPasswordField
import com.proyecpg.hartarte.ui.components.customTextField

@Composable
fun RegisterScreen(
    state: RegisterState,
    navigateToLogin : () -> Unit,
    onRegisterEvent: (RegisterEvent) -> Unit,
    onDismissDialog: () -> Unit
){

    var username: String
    var email: String
    var password: String
    var passwordConfirmation: String

    EventDialog(
        errorMessage = state.registerError.toString(),
        onDismiss = onDismissDialog,
        showDialog = state.registerError != null
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp, vertical = 40.dp)
    ) {

        item {
            IconButton(onClick = navigateToLogin) {
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(intrinsicSize = IntrinsicSize.Min),
                horizontalArrangement = Arrangement.Center
            ) {
                ProgressButton(
                    stringResource(id = R.string.register),
                    state.isLoading ,
                    onEventClick = {
                        onRegisterEvent(
                            RegisterEvent.RegisterClicked(
                                username = username,
                                email = email,
                                password = password,
                                confirmPassword = passwordConfirmation
                            )
                        )
                    }
                )
            }
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
    RegisterScreen(
        state = RegisterState(isLoading = false),
        navigateToLogin = {},
        onRegisterEvent = {},
        onDismissDialog = {}
    )
}