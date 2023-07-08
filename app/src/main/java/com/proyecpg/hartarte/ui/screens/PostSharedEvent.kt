package com.proyecpg.hartarte.ui.screens

sealed class PostSharedEvent{
    class OnLiked(val postId : String, val like : Boolean) : PostSharedEvent()

    class OnBookmarked(val postId: String, val bookmark : Boolean) : PostSharedEvent()
}
