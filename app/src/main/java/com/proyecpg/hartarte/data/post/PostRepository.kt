package com.proyecpg.hartarte.data.post

import androidx.paging.PagingData
import com.proyecpg.hartarte.domain.model.Post
import com.proyecpg.hartarte.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(): Flow<PagingData<Post>>

    suspend fun isPostLiked(postId: String): Resource<Boolean>

    suspend fun registerLike(postId: String, liked: Boolean): Resource<Boolean>
}