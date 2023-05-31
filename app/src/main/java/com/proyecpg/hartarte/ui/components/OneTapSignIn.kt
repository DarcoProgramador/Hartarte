package com.proyecpg.hartarte.ui.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.proyecpg.hartarte.ui.screens.AuthViewModel
import com.proyecpg.hartarte.utils.Constants.TAG
import com.proyecpg.hartarte.utils.Resource

@Composable
fun OneTapSignIn(
    viewModel: AuthViewModel = hiltViewModel(),
    launch: (result: BeginSignInResult) -> Unit
) {
    when(val oneTapSignInResponse = viewModel.oneTapSignInResponse) {
        is Resource.Loading -> {/*TODO:Hacer que cargue el boton de google*/}
        is Resource.Success -> oneTapSignInResponse.result?.let {
            LaunchedEffect(it) {
                launch(it)
            }
        }
        is Resource.Failure -> LaunchedEffect(Unit) {
            Log.e(TAG, oneTapSignInResponse.exception.toString())
        }
    }
}