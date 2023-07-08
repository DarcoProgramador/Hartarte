package com.proyecpg.hartarte.ui.screens

sealed class PostSharedEvent{
    class onLiked(val postId : String) : PostSharedEvent()

    class onBookmarked(val postId: String) : PostSharedEvent()
}
