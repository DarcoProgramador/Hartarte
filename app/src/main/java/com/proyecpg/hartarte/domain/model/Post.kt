package com.proyecpg.hartarte.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
data class Post (
    @DocumentId val id: String,
    val titulo: String = "",
    val descripcion: String = "",
    val imagen: String? = null,
    val userRef: String? = "",
    val creation_time: Timestamp? = null
)

data class Comentarios(
    val comentario: String,
    val userRef: String
)
