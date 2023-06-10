package com.proyecpg.hartarte.data.post

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.proyecpg.hartarte.data.paging.PostPagingSource
import com.proyecpg.hartarte.domain.model.Post
import com.proyecpg.hartarte.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImp @Inject constructor(
    private val source: PostPagingSource,
    private val config: PagingConfig
): PostRepository {

    override fun getPosts(): Flow<PagingData<Post>> = Pager(
        config = config
    ) {
        source
    }.flow

    override suspend fun isPostLiked(postId: String, uid: String): Resource<Boolean> {
        TODO("Create funtion is post Liked comprobation")
    }

    override suspend fun registerLike(postId: String, liked: Boolean): Resource<Boolean> {
        TODO("Create funtion register like in post")
    }


}
