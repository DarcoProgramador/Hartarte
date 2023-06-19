package com.proyecpg.hartarte.data.model

import android.provider.ContactsContract.DisplayPhoto
import com.google.firebase.Timestamp

data class User(
    val email : String? = null,
    val photo: String? = null,
    val username : String? = null,
    val createdAt: Timestamp? = null
)