package com.proyecpg.hartarte.ui.model

data class PostUI(
    val postId : String? = "",
    val images : List<String> = emptyList(),
    val title : String? = "",
    val description : String? = "",
    val date : String? = "",
    val likes : Int? = 0
)
