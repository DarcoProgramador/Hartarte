package com.proyecpg.hartarte.data.post

import android.net.Uri
import androidx.paging.PagingData
import com.proyecpg.hartarte.domain.model.Post
import com.proyecpg.hartarte.utils.QueryParams
import com.proyecpg.hartarte.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPostsBy(query : QueryParams): Flow<PagingData<Post>>

    suspend fun getBookmarkPostsIds(): Resource<List<String>>

    suspend fun registerLike(postId: String, liked: Boolean): Resource<Boolean>

    suspend fun registerBookmark(postId: String, bookmarked: Boolean): Resource<Boolean>

    suspend fun createPost(images : List<Uri>,
                           titulo: String,
                           descripcion: String
    ): Resource<Boolean>
}