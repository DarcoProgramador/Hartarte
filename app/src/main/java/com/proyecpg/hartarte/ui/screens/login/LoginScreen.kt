package com.proyecpg.hartarte.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecpg.hartarte.R
import com.proyecpg.hartarte.ui.components.EventDialog
import com.proyecpg.hartarte.ui.components.GoogleLoginButton
import com.proyecpg.hartarte.ui.components.ProgressButton
import com.proyecpg.hartarte.ui.components.customPasswordField
import com.proyecpg.hartarte.ui.components.customTextField

@Composable
fun LoginScreen(
    state: LoginState,
    onEventLogin : (LoginEvent) -> Unit,
    onSignInGoogleClick: () -> Unit,
    navigateToRegister: () -> Unit,
    onDismissDialog: () -> Unit
) {
    var email: String
    var password: String

    val focusManager = LocalFocusManager.current

    EventDialog(
        errorMessage = state.loginError.toString(),
        onDismiss = onDismissDialog,
        showDialog = state.loginError != null
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Image(
                modifier = Modifier.size(180.dp),
                painter = painterResource(id = R.drawable.img_logo),
                contentDescription = stringResource(R.string.app_name)
            )
            Text(
                text = "Hartarte",
                style = TextStyle(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.size(50.dp))

            Column(
                modifier = Modifier.padding(horizontal = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    email = customTextField( stringResource(id = R.string.email), focusManager )

                    Spacer(modifier = Modifier.size(30.dp))

                    password = customPasswordField( stringResource(id = R.string.password), focusManager, true )

                    Spacer(modifier = Modifier.size(30.dp))

                    ProgressButton(
                        stringResource(id = R.string.login),
                        state.isLoading,
                        onEventClick = {
                            onEventLogin(
                                LoginEvent.LoginClicked(
                                    email = email,
                                    password = password
                                )
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.size(10.dp))

                Text(
                    text = stringResource(id = R.string.no_account),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.clickable {
                        navigateToRegister()
                    }
                )

                Spacer(modifier = Modifier.size(60.dp))

                GoogleLoginButton(onSignInGoogleClick)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen(){
    LoginScreen(state = LoginState(isLoading = false),
        onEventLogin = {},
        onSignInGoogleClick = {},
        navigateToRegister = {},
        onDismissDialog = {}
    )
}
