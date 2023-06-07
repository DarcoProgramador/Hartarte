package com.proyecpg.hartarte.data.auth

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.proyecpg.hartarte.utils.Resource

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun signup(name: String, email: String, password: String): Resource<FirebaseUser>
    suspend fun singInWithCredentials(credentials: AuthCredential): Resource<FirebaseUser>

    suspend fun oneTapSignInWithGoogle(): Resource<BeginSignInResult>
    suspend fun logout()
}