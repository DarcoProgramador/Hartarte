package com.proyecpg.hartarte.data.auth

import com.google.firebase.auth.FirebaseUser
import com.proyecpg.hartarte.vo.Resource

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun signup(name: String, email: String, password: String): Resource<FirebaseUser>
    fun logout()
}