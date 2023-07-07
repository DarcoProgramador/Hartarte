package com.proyecpg.hartarte.ui.model

data class PostUI(
    val postId : String = "",
    val images : List<String> = emptyList(),
    val username : String = "",
    val userPic: String = "",
    val title : String = "",
    val description : String = "",
    val date : String = "",
    val likesCount : Int = 0
)
