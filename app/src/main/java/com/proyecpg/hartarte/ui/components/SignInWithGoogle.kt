package com.proyecpg.hartarte.ui.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.proyecpg.hartarte.ui.screens.AuthViewModel
import com.proyecpg.hartarte.utils.Constants
import com.proyecpg.hartarte.utils.Resource

@Composable
fun SignInWithGoogle(
    viewModel: AuthViewModel = hiltViewModel(),
    navigateToHomeScreen: (signedIn: Boolean) -> Unit
) {
    when(val signInWithGoogleResponse = viewModel.signInWithGoogleResponse) {
        is Resource.Loading -> {}
        is Resource.Success -> signInWithGoogleResponse.result?.let { signedIn ->
            LaunchedEffect(signedIn) {
                if (signedIn != null){
                    navigateToHomeScreen(true)
                }
            }
        }
        is Resource.Failure -> LaunchedEffect(Unit) {
            Log.e(Constants.TAG, signInWithGoogleResponse.exception.toString())
        }
    }
}