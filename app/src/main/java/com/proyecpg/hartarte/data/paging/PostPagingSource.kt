package com.proyecpg.hartarte.data.paging
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.proyecpg.hartarte.domain.model.Post
import com.proyecpg.hartarte.utils.Constants
import kotlinx.coroutines.tasks.await


class PostPagingSource (
    private val queryPostByCreationTime: Query,
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : PagingSource<QuerySnapshot, Post>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, Post>): QuerySnapshot? = null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Post> = try {
        val currentPage = params.key ?: queryPostByCreationTime.get().await()
        val lastVisiblePost = currentPage.documents[currentPage.size() - 1]
        val nextPage = queryPostByCreationTime.startAfter(lastVisiblePost).get().await()

        LoadResult.Page(
            data = getPost(currentPage),
            prevKey = null,
            nextKey = if(!nextPage.isEmpty) nextPage else null
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    private suspend fun getPost(currentPage : QuerySnapshot) : List<Post>{
        val postList = currentPage.toObjects(Post::class.java)

        postList.forEach {post ->
            val postId = post.postId?:""

            val resultLike = isPostLiked(postId)
            val resultBookMark = isPostBookmarked(postId)

            post.liked = resultLike
            post.bookmarked = resultBookMark
        }

        return  postList
    }

    private suspend fun isPostLiked(postId: String): Boolean {
        try {
            val post = db.collection(Constants.POST_LIKES_COLLECTION).document(postId).get().await()
            val user = firebaseAuth.currentUser!!.uid
            if (!post.exists()) return false
            val likeArray: List<String> = post.get(Constants.LIKES) as List<String>
            return likeArray.contains(user)
        } catch (e: Exception) {
            return false
        }
    }

    private suspend fun isPostBookmarked(postId: String): Boolean {
        try {
            val post = db.collection(Constants.POST_BOOKMARKS_COLLECTION).document(postId).get().await()
            val user = firebaseAuth.currentUser!!.uid
            if (!post.exists()) return false
            val bookmarkArray: List<String> = post.get(Constants.BOOKMARKS) as List<String>
            return bookmarkArray.contains(user)
        } catch (e: Exception) {
            return false
        }
    }
}
