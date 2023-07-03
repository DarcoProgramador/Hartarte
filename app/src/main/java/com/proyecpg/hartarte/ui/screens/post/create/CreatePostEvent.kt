package com.proyecpg.hartarte.ui.screens.post.create

import android.net.Uri

sealed class CreatePostEvent{
    class CreatePostClicked(val images : List<Uri>, val titulo: String, val descripcion: String) : CreatePostEvent()
}