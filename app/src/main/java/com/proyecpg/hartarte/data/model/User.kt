package com.proyecpg.hartarte.data.model

import com.google.firebase.Timestamp
import com.proyecpg.hartarte.ui.model.UserUI

data class User(
    val email : String? = null,
    val photoUrl: String? = null,
    val username : String? = null,
    val createdAt: Timestamp? = null
)

fun User.toUserUI(): UserUI = UserUI(email = email, photo = photoUrl, username = username)