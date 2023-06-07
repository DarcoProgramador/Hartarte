package com.proyecpg.hartarte.data.paging
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.proyecpg.hartarte.domain.model.Post
import kotlinx.coroutines.tasks.await


class PostPagingSource (
    private val queryPostByCreationTime: Query
) : PagingSource<QuerySnapshot, Post>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, Post>): QuerySnapshot? = null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Post> = try {
        val currentPage = params.key ?: queryPostByCreationTime.get().await()
        val lastVisiblePost = currentPage.documents[currentPage.size() - 1]
        val nextPage = queryPostByCreationTime.startAfter(lastVisiblePost).get().await()


        LoadResult.Page(
            data = currentPage.toObjects(Post::class.java),
            prevKey = null,
            nextKey = if(!nextPage.isEmpty) nextPage else null
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }
}
