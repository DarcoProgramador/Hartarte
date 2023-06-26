package com.proyecpg.hartarte.data.user

import com.google.firebase.auth.FirebaseUser
import com.proyecpg.hartarte.data.model.User

interface UserRepository {
    val currentUser: FirebaseUser?

    suspend fun getUser() : User
}