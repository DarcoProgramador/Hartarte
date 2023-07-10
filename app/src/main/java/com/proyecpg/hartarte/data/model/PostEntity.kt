package com.proyecpg.hartarte.data.model
import com.google.firebase.Timestamp
import com.proyecpg.hartarte.domain.model.User

data class PostEntity(
    val titulo: String? = "",
    val descripcion: String? = "",
    val images: ArrayList<String>? = null,
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

fun UserHashmap.toUser() : User = User(uid = uid, name = name, photo = photo)
