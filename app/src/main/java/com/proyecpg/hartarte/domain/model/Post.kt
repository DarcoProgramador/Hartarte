package com.proyecpg.hartarte.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
data class Post (
    @DocumentId val postId: String? = null,
    val titulo: String? = null,
    val descripcion: String? = null,
    val imagen: String? = null,
    val likes: Long? = null,
    val user: User? = User(),
    val createdAt: Timestamp? = null
)

data class User(
    val uid: String? = null,
    val name: String? = null,
    val photo: String? = null
)

