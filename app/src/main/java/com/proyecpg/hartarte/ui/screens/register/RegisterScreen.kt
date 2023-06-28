package com.proyecpg.hartarte.ui.screens.register

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecpg.hartarte.R
import com.proyecpg.hartarte.ui.components.EventDialog
import com.proyecpg.hartarte.ui.components.ProgressButton
import com.proyecpg.hartarte.ui.components.customPasswordField
import com.proyecpg.hartarte.ui.components.customTextField

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.register),
                        modifier = Modifier.padding(start = 20.dp)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigateToLogin,
                        modifier = Modifier.padding(start = 20.dp, top = 20.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                }
            )
        }
    ){ innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 20.dp)
        ) {

            item {
                val focusManager = LocalFocusManager.current

                TitleText( stringResource(id = R.string.title_user) )

                username = customTextField( stringResource(id = R.string.username), focusManager )

                Spacer(modifier = Modifier.size(30.dp))

                TitleText( stringResource(id = R.string.title_email) )

                email = customTextField( stringResource(id = R.string.email), focusManager )

                Spacer(modifier = Modifier.size(30.dp))

                TitleText( stringResource(id = R.string.title_password) )

                password = customPasswordField( stringResource(id = R.string.password), focusManager, false )

                Spacer(modifier = Modifier.size(30.dp))

                TitleText( stringResource(id = R.string.title_password_confirm) )

                passwordConfirmation = customPasswordField( stringResource(id = R.string.password_confirm), focusManager, true )

                Spacer(modifier = Modifier.size(35.dp))

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