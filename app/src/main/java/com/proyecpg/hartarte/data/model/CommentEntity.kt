package com.proyecpg.hartarte.data.model

import com.google.firebase.Timestamp

data class CommentEntity(
    val comment : String? = "",
    val uid : String? = "",
    val createAt : Timestamp = Timestamp.now()
)