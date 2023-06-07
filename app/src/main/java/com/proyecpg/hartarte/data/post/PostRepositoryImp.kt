package com.proyecpg.hartarte.data.post

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.proyecpg.hartarte.data.paging.PostPagingSource
import com.proyecpg.hartarte.domain.model.Post
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
}
