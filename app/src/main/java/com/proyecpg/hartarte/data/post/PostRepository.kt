package com.proyecpg.hartarte.data.post

import android.net.Uri
import androidx.paging.PagingData
import com.google.firebase.firestore.Query
import com.proyecpg.hartarte.domain.model.Comment
import com.proyecpg.hartarte.domain.model.Post
import com.proyecpg.hartarte.utils.QueryParams
import com.proyecpg.hartarte.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPostsBy(query : QueryParams): Flow<PagingData<Post>>

    suspend fun getPostById(postId: String) : Resource<Post>

    suspend fun getPostBookmarkedQuery(): Resource<Query>

    suspend fun registerLike(postId: String, liked: Boolean): Resource<Boolean>

    suspend fun registerBookmark(postId: String, bookmarked: Boolean): Resource<Boolean>

    suspend fun getLike(postId: String) : Resource<Boolean>

    suspend fun getBookmark(postId: String) : Resource<Boolean>

    suspend fun createPost(images : List<Uri>,
                           titulo: String,
                           descripcion: String
    ): Resource<Boolean>

    suspend fun getComments(postId: String): Resource<List<Comment>>

    suspend fun registerComment(postId: String, comment : String) : Resource<Boolean>
}