package com.proyecpg.hartarte.domain.model

import com.google.firebase.Timestamp

data class Comment(
    val comment : String? = "",
    val uid : String? = "",
    val username : String? = "",
    val photo : String? = "",
    val date : Timestamp? = Timestamp.now()
)
