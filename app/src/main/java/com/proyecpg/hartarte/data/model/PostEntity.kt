package com.proyecpg.hartarte.data.model
import com.google.firebase.Timestamp

data class PostEntity(
    val titulo: String? = "",
    val descripcion: String? = "",
    val imagen: String? = "",
    val likes: Long? = 0L,
    val bookmarks: Long? = 0L,
    val user: UserHashmap? = UserHashmap(),
    val createdAt: Timestamp? = Timestamp.now()
)

data class UserHashmap(
    val uid: String? = "",
    val name: String? = "",
    val photo: String? = ""
)
