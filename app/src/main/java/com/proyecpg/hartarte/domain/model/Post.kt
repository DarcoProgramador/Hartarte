package com.proyecpg.hartarte.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
data class Post (
    @DocumentId val postId: String,
    val titulo: String = "",
    val descripcion: String = "",
    val imagen: String? = null,
    val userUID: String? = "",
    val userName: String? = "",
    val userPhoto: String? = "",
    val likes: Long? = 0L,
    val createdAt: Timestamp? = null
)

