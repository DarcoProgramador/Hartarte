package com.proyecpg.hartarte.data.user

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.proyecpg.hartarte.data.model.User
import com.proyecpg.hartarte.ui.model.UserUI
import com.proyecpg.hartarte.utils.Resource

interface UserRepository {
    val currentUser: FirebaseUser?

    suspend fun getUser() : User

    suspend fun editUser(username : String, descripcion : String) : Resource<Boolean>

    suspend fun updateUserPhoto(photo : Uri) : Resource<Boolean>

    suspend fun getUserByUID(uid : String) : Resource<User>
}