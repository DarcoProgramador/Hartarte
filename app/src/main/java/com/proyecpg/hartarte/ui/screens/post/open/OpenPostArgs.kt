package com.proyecpg.hartarte.ui.screens.post.open

data class OpenPostArgs(
    val postId: String = "",
    val userId: String = "",
    val postImages: List<String> = emptyList(),
    val postUsername: String = "",
    val postUserPic: String = "",
    val postTitle: String = "",
    val postDescription: String = "",
    val postDate: String = "",
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false,
    val likesCount: Int = 0
)