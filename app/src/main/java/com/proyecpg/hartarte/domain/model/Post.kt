package com.proyecpg.hartarte.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.proyecpg.hartarte.ui.screens.post.open.OpenPostArgs
import java.text.SimpleDateFormat

data class Post (
    @DocumentId val postId: String? = null,
    val titulo: String? = null,
    val descripcion: String? = null,
    val images : ArrayList<String>? = null,
    var liked: Boolean? = null,
    var bookmarked : Boolean? = null,
    val likes: Long? = null,
    val bookmarks: Long? = null,
    val user: User? = User(),
    val createdAt: Timestamp? = null
)

data class User(
    val uid: String? = null,
    val name: String? = null,
    val photo: String? = null
)

fun Post.toOpenPostArgs() : OpenPostArgs {
    val imgs = images?: arrayListOf()
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    val time = createdAt?.toDate()
    val date = if(time != null) formatter.format(time) else "00/00/0000 00:00:00"
    val likes = likes?:0L

    return OpenPostArgs(
        postId = postId?:"",
        userId = user?.uid?:"",
        postImages = imgs.toList(),
        postUsername = user?.name?:"",
        postUserPic = user?.photo?:"",
        postTitle = titulo?:"",
        postDescription = descripcion?:"",
        postDate = date,
        likesCount = likes.toInt(),
        isBookmarked = bookmarked?:false,
        isLiked = liked?:false
    )
}