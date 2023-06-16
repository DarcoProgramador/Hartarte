package com.proyecpg.hartarte.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
data class Post (
    @DocumentId val postId: String,
    val titulo: String = "",
    val descripcion: String = "",
    val imagen: String? = null,
    val likes: Long? = 0L,
    val user: User? = User(),
    val createdAt: Timestamp? = null
)

data class User(
    val uid: String = "",
    val name: String = "",
    val photo: String = ""
)

