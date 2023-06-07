package com.proyecpg.hartarte.data.post

import androidx.paging.PagingData
import com.proyecpg.hartarte.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(): Flow<PagingData<Post>>
}