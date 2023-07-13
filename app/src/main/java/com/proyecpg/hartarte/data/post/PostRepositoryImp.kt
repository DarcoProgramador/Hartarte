package com.proyecpg.hartarte.data.post

import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.proyecpg.hartarte.data.model.CommentEntity
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
import com.proyecpg.hartarte.data.model.toUser
import com.proyecpg.hartarte.domain.model.Comment
import com.proyecpg.hartarte.utils.Constants
import com.proyecpg.hartarte.utils.Constants.COMMENT_COLLECTION
import com.proyecpg.hartarte.utils.Constants.POST_IMAGES
import com.proyecpg.hartarte.utils.Constants.POST_PATH
import com.proyecpg.hartarte.utils.Constants.USERS
import com.proyecpg.hartarte.utils.QueryParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImp @Inject constructor(
    private val config: PagingConfig,
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage : FirebaseStorage
): PostRepository {

    override fun getPostsBy(query: QueryParams): Flow<PagingData<Post>> = Pager(
        config = config
    ) {
        PostPagingSource(
            queryPost = query,
            firebaseAuth = firebaseAuth,
            db = db
        )
    }.flow

    override suspend fun getPostById(postId: String): Resource<Post> {
        return try {
            val user = firebaseAuth.currentUser!!.uid

            val postRef = db.collection(POST_COLLECTION).document(postId).get().await()
            val post = postRef.toObject(PostEntity::class.java)

            val postLikesRef = db.collection(POST_LIKES_COLLECTION).document(postId).get().await()
            val liked = if (!postLikesRef.exists()){
                false
            }else{
                val likeArray: List<String> = postLikesRef.get(LIKES) as List<String>
                likeArray.contains(user)
            }

            val postBookmarkRef = db.collection(POST_BOOKMARKS_COLLECTION).document(postId).get().await()
            val bookmarked = if(!postBookmarkRef.exists()){
                false
            }else{
                val bookmarkArray: List<String> = postBookmarkRef.get(BOOKMARKS) as List<String>
                bookmarkArray.contains(user)
            }

            //return Post
            Resource.Success(Post(
                postId = postId,
                titulo = post?.titulo,
                descripcion = post?.descripcion,
                images = post?.images,
                liked = liked,
                bookmarked = bookmarked,
                likes = post?.likes,
                bookmarks = post?.bookmarks,
                user = post?.user?.toUser(),
                createdAt = post?.createdAt
            ))

        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override suspend fun getPostBookmarkedQuery(): Resource<Query> {
       return try {
           val bookMarkRef = db.collection(POST_BOOKMARKS_COLLECTION)
           val userUID = firebaseAuth.currentUser?.uid.toString()
           val postIdsRef = bookMarkRef.whereArrayContains(BOOKMARKS, userUID).get().await()

           val postIds = postIdsRef.map { it.id }

           val postRef = db.collection(POST_COLLECTION)

           val query = postRef.whereIn(FieldPath.documentId(), postIds).limit(Constants.PAGE_SIZE)

           Resource.Success(query)
       } catch (e: Exception) {
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

            var exeption : Exception? = null

            db.runTransaction { transaction ->
                val snapshot = transaction.get(postRef)
                val likeCount = snapshot.getLong(LIKES)!!
                val likesArraysRef = transaction.get(postLikesRef).get(LIKES)
                //contador de likes mayor a -1
                if (likeCount >= 0){
                    //si no existe crealo
                    if (likesArraysRef == null){
                        transaction.set(postLikesRef, hashMapOf(LIKES to arrayListOf(user)), SetOptions.merge())
                        transaction.update(postRef, LIKES, increment)
                        return@runTransaction
                    }

                    val likesArrays: List<String> = likesArraysRef as List<String>

                    //si es un dislike ponlo
                    if(!liked && likesArrays.contains(user)){
                        transaction.update(postRef, LIKES, decrement)
                        transaction.update(postLikesRef, LIKES, FieldValue.arrayRemove(user))
                        return@runTransaction
                    }
                    //si es un like ponlo
                    if (!likesArrays.contains(user)){
                        transaction.update(postLikesRef, LIKES, FieldValue.arrayUnion(user))
                        transaction.update(postRef, LIKES, increment)
                    }
                }
            }.addOnSuccessListener {
                //TODO: EMIT Result when the transaccion is true
            }.addOnFailureListener {
                exeption = it
            }.await()
            if (exeption != null){
                throw Exception(exeption)
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

            var exeption : Exception? = null

            db.runTransaction { transaction ->
                val snapshot = transaction.get(postRef)
                val bookmarksCount = snapshot.getLong(BOOKMARKS)!!
                val bookmarkesArraysRef = transaction.get(postBookmarksRef).get(BOOKMARKS)
                //contador de bookmark mayor a -1
                if (bookmarksCount >= 0){
                    //si no existe crealo
                    if (bookmarkesArraysRef == null){
                        transaction.set(postBookmarksRef, hashMapOf(BOOKMARKS to arrayListOf(user)), SetOptions.merge())
                        transaction.update(postRef, BOOKMARKS, increment)
                        return@runTransaction
                    }

                    val bookmarksArray: List<String> = bookmarkesArraysRef as List<String>

                    //si es un dislike ponlo
                    if(!bookmarked && bookmarksArray.contains(user)){
                        transaction.update(postRef, BOOKMARKS, decrement)
                        transaction.update(postBookmarksRef, BOOKMARKS, FieldValue.arrayRemove(user))
                        return@runTransaction
                    }
                    //si es un like ponlo
                    if (!bookmarksArray.contains(user)){
                        transaction.update(postBookmarksRef, BOOKMARKS, FieldValue.arrayUnion(user))
                        transaction.update(postRef, BOOKMARKS, increment)
                    }
                }
            }.addOnSuccessListener {
                //TODO: EMIT Result when the transaccion is true
            }.addOnFailureListener {
                exeption = it
            }.await()

            if (exeption != null){
                throw Exception(exeption)
            }
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override suspend fun getLike(postId: String): Resource<Boolean> {
        try {
            val post = db.collection(POST_LIKES_COLLECTION).document(postId).get().await()
            val user = firebaseAuth.currentUser!!.uid
            if (!post.exists()) return Resource.Success(false)
            val likeArray: List<String> = post.get(LIKES) as List<String>
            return Resource.Success(likeArray.contains(user))
        } catch (e: Exception) {
            return Resource.Failure(e)
        }
    }

    override suspend fun getBookmark(postId: String): Resource<Boolean> {
        try {
            val post = db.collection(POST_BOOKMARKS_COLLECTION).document(postId).get().await()
            val user = firebaseAuth.currentUser!!.uid
            if (!post.exists()) return Resource.Success(false)
            val bookmarkArray: List<String> = post.get(BOOKMARKS) as List<String>
            return Resource.Success(bookmarkArray.contains(user))
        } catch (e: Exception) {
            return Resource.Failure(e)
        }
    }

    override suspend fun createPost(
        images: List<Uri>,
        titulo: String,
        descripcion: String
    ): Resource<Boolean> {
        return try {
            val storageRef = storage.reference

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

            val postRef =  db.collection(POST_COLLECTION)
            //add post
            val newPostRef = postRef.add(newPost).await()

            for ((index, image) in images.withIndex()){
                //Create Ref to storage
                val pathImage = "${POST_PATH}photo${userUID}${newPostRef.id}_${index}.jpg"
                val postImgRef = storageRef.child(pathImage)

                //upload image
                val imageStorageRef = postImgRef.putFile(image).await()
                val imageURL = imageStorageRef.storage.downloadUrl.await()

                val url = imageURL.toString()
                //update value of images
                postRef.document(newPostRef.id).update(POST_IMAGES, FieldValue.arrayUnion(url)).await()
            }
            
            Resource.Success(true)

        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override suspend fun getComments(postId: String): Resource<List<Comment>> {
        return try {
            val commentsRef = db.collection(POST_COLLECTION).document(postId).collection(COMMENT_COLLECTION)
            val userRef = db.collection(USERS)

            val commentsDocs = commentsRef.get().await()

            val commentsEntity = commentsDocs.toObjects(CommentEntity::class.java)

            val comments : MutableList<Comment> = mutableListOf()
            for(comment in commentsEntity){
                if (comment.uid.isNullOrEmpty()){
                    continue
                }
                val userDoc = userRef.document(comment.uid).get().await()
                val user = userDoc.toObject(User::class.java) ?: continue

                if(user.username.isNullOrEmpty() || user.photoUrl.isNullOrEmpty()){
                    continue
                }

                comments.add(Comment(
                    comment = comment.comment,
                    uid = comment.uid,
                    username = user.username,
                    photo = user.photoUrl,
                    date = comment.createAt
                ))
            }

            Resource.Success(comments.toList())
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override suspend fun registerComment(postId: String, comment: String): Resource<Boolean> {
        return try {
            val user = firebaseAuth.currentUser!!.uid
            val commentRef = db.collection(POST_COLLECTION).document(postId).collection(COMMENT_COLLECTION)
            val newComment = CommentEntity(
                comment = comment,
                uid = user
            )
            //add new comment
            commentRef.add(newComment).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

}
