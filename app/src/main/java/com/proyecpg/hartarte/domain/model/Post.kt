package com.proyecpg.hartarte.domain.model

data class Post (
    val titulo: String,
    val descipcion: String,
    val imagen: String? = null,
    val userRef: String,
    val comentarios : Comentarios,
    val likes: Likes
)

data class Comentarios(
    val comentario: String,
    val userRef: String
)

data class Likes(
    val userRef: String
)
