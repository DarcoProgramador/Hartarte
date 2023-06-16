package com.proyecpg.hartarte.data.post

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.proyecpg.hartarte.data.paging.PostPagingSource
import com.proyecpg.hartarte.domain.model.Post
import com.proyecpg.hartarte.utils.Constants.LIKES
import com.proyecpg.hartarte.utils.Constants.POST_COLLECTION
import com.proyecpg.hartarte.utils.Constants.POST_LIKES_COLLECTION
import com.proyecpg.hartarte.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.security.KeyStore.TrustedCertificateEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImp @Inject constructor(
    private val source: PostPagingSource,
    private val config: PagingConfig,
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
): PostRepository {

    override fun getPosts(): Flow<PagingData<Post>> = Pager(
        config = config
    ) {
        source
    }.flow

    override suspend fun isPostLiked(postId: String): Resource<Boolean> {
        return try {
            val post = db.collection(POST_LIKES_COLLECTION).document(postId).get().await()
            val user = firebaseAuth.currentUser!!.uid
            if (!post.exists()) Resource.Success(false)
            val likeArray: List<String> = post.get(LIKES) as List<String>
            Resource.Success(likeArray.contains(user))
        }catch (e : Exception){
            Resource.Failure(e)
        }
    }

    override suspend fun registerLike(postId: String, liked: Boolean): Resource<Boolean> {
        return try {
            val increment = FieldValue.increment(1)
            val decrement = FieldValue.increment(-1)

            val user = firebaseAuth.currentUser!!.uid

            val postRef = db.collection(POST_COLLECTION).document(postId)
            val postLikesRef = db.collection(POST_LIKES_COLLECTION).document(postId)

            db.runTransaction { transaction ->
                val snapshot = transaction.get(postRef)
                val likeCount = snapshot.getLong(LIKES)!!
                //contador de likes mayor a -1
                if (likeCount >= 0){
                    //si no existe crealo
                    if (!transaction.get(postLikesRef).exists()){
                        transaction.set(postLikesRef, hashMapOf(LIKES to arrayListOf(user)), SetOptions.merge())
                        transaction.update(postRef, LIKES, increment)
                        return@runTransaction
                    }
                    //si es un dislike ponlo
                    if(!liked){
                        transaction.update(postRef, LIKES, decrement)
                        transaction.update(postLikesRef, LIKES, FieldValue.arrayRemove(user))
                        return@runTransaction
                    }
                    //si es un like ponlo
                    transaction.update(postLikesRef, LIKES, FieldValue.arrayUnion(user))
                    transaction.update(postRef, LIKES, increment)
                }
            }.addOnSuccessListener {
                //TODO: EMIT Result when the transaccion is true
            }.addOnFailureListener {
                throw Exception(it.message)
            }
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

}
