package com.proyecpg.hartarte.ui.screens

sealed class PostSharedEvent{
    class onLiked(val postId : String, val liked : Boolean) : PostSharedEvent()

    class onBookmarked(val postId: String, val bookmarked : Boolean) : PostSharedEvent()
}
