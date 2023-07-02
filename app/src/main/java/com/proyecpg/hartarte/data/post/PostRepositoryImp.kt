package com.proyecpg.hartarte.data.post

import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.proyecpg.hartarte.data.paging.PostPagingSource
import com.proyecpg.hartarte.domain.model.Post
import com.proyecpg.hartarte.utils.Constants.BOOKMARKS
import com.proyecpg.hartarte.utils.Constants.LIKES
import com.proyecpg.hartarte.utils.Constants.POST_BOOKMARKS_COLLECTION
import com.proyecpg.hartarte.utils.Constants.POST_COLLECTION
import com.proyecpg.hartarte.utils.Constants.POST_LIKES_COLLECTION
import com.proyecpg.hartarte.utils.Resource
import com.proyecpg.hartarte.data.model.PostEntity
import com.proyecpg.hartarte.data.model.User
import com.proyecpg.hartarte.data.model.UserHashmap
import com.proyecpg.hartarte.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
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

    override suspend fun registerBookmark(postId: String, bookmarked: Boolean): Resource<Boolean> {
        return try {
            val increment = FieldValue.increment(1)
            val decrement = FieldValue.increment(-1)

            val user = firebaseAuth.currentUser!!.uid

            val postRef = db.collection(POST_COLLECTION).document(postId)
            val postBookmarksRef = db.collection(POST_BOOKMARKS_COLLECTION).document(postId)

            db.runTransaction { transaction ->
                val snapshot = transaction.get(postRef)
                val bookmarksCount = snapshot.getLong(BOOKMARKS)!!
                //contador de bookmark mayor a -1
                if (bookmarksCount >= 0){
                    //si no existe crealo
                    if (!transaction.get(postBookmarksRef).exists()){
                        transaction.set(postBookmarksRef, hashMapOf(BOOKMARKS to arrayListOf(user)), SetOptions.merge())
                        transaction.update(postRef, BOOKMARKS, increment)
                        return@runTransaction
                    }
                    //si es un dislike ponlo
                    if(!bookmarked){
                        transaction.update(postRef, BOOKMARKS, decrement)
                        transaction.update(postBookmarksRef, BOOKMARKS, FieldValue.arrayRemove(user))
                        return@runTransaction
                    }
                    //si es un like ponlo
                    transaction.update(postBookmarksRef, BOOKMARKS, FieldValue.arrayUnion(user))
                    transaction.update(postRef, BOOKMARKS, increment)
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

    override suspend fun createPost(
        images: List<Uri>,
        titulo: String,
        descripcion: String
    ): Resource<Boolean> {
        return try {
            val userUID = firebaseAuth.currentUser?.uid.toString()
            val userRef = db.collection(Constants.USERS).document(userUID).get().await()
            val user = userRef.toObject(User::class.java)

            val newPost = PostEntity(
                titulo = titulo,
                descripcion = descripcion,
                user = UserHashmap(
                    uid = userUID,
                    photo = user?.photoUrl,
                    name = user?.username
                )
            )

            val newPostRef = db.collection(POST_COLLECTION).add(newPost).await()
            Resource.Success(true)
            
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

}
