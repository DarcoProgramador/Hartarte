package com.proyecpg.hartarte.data.user

import com.google.firebase.auth.FirebaseUser
import com.proyecpg.hartarte.data.model.User
import com.proyecpg.hartarte.ui.model.UserUI
import com.proyecpg.hartarte.utils.Resource

interface UserRepository {
    val currentUser: FirebaseUser?

    suspend fun getUser() : User

    suspend fun editUser(username : String, descripcion : String, photo : String) : Resource<Boolean>
}