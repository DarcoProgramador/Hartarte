package com.proyecpg.hartarte.ui.screens.post.create

data class CreatePostState(
    val isCreatePostSuccessful: Boolean = false,
    val createPostError: String? = null,
    val isLoading: Boolean = false
)