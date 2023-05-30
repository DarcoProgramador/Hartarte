package com.proyecpg.hartarte.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.proyecpg.hartarte.R
import com.proyecpg.hartarte.ui.screens.AuthViewModel
import kotlinx.coroutines.coroutineScope

@Composable
fun HomeScreen(
    viewModel : AuthViewModel,
    navigateToAuthScrens: () -> Unit
    //OnRegisterClick
) {
    val state by viewModel.logged.collectAsStateWithLifecycle()
    state.let {
        if (it){
            HomeScreenContent()
        }else{
            navigateToAuthScrens()
        }
    }
}

@Composable
fun HomeScreenContent(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(150.dp),
            painter = painterResource(id = R.drawable.img_logo),
            contentDescription = stringResource(R.string.app_name)
        )

        Spacer(modifier = Modifier.size(50.dp))

        Text(text = "HomeScreen")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen(){
    HomeScreenContent()
}