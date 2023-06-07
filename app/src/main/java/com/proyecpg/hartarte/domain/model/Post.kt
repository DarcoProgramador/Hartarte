package com.proyecpg.hartarte.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.local.ReferenceSet

data class Post (
    val titulo: String = "",
    val descipcion: String = "",
    val imagen: String? = null,
    val userRef: String? = "",
    val creation_time: Timestamp? = null
)

data class Comentarios(
    val comentario: String,
    val userRef: String
)
