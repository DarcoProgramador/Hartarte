package com.proyecpg.hartarte.ui.screens.post.open

data class OpenPostArgs(
    val postId: String,
    val postImages: List<String>,
    val postUsername: String,
    val postUserPic: String,
    val postTitle: String,
    val postDescription: String,
    val postDate: String,
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val likesCount: Int
)