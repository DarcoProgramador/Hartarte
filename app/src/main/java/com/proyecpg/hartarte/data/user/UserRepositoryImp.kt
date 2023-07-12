package com.proyecpg.hartarte.data.user

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.proyecpg.hartarte.data.model.User
import com.proyecpg.hartarte.utils.Constants.PHOTO_URL
import com.proyecpg.hartarte.utils.Constants.USERS
import com.proyecpg.hartarte.utils.Constants.USER_PATH
import com.proyecpg.hartarte.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage : FirebaseStorage
) : UserRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    //TODO:Hacer que la funcion devuelva los datos en tiempo real
    override suspend fun getUser(): User {
        return try {
            val uid = currentUser?.uid.toString()
            val userRef = db.collection(USERS).document(uid).get().await()
            val user = userRef.toObject(User::class.java)
            user?:User()
        }catch (e: Exception){
            //e.printStackTrace()
            User()
        }
    }

    override suspend fun editUser(username : String, descripcion : String): Resource<Boolean> {
        return  try {
            val uid = currentUser?.uid.toString()

            val userRef = db.collection(USERS).document(uid)

            val userUpdates = mapOf<String, Any>(
                "username" to username,
                "descripcion" to descripcion,
            )

            userRef.update(userUpdates).await()

            Resource.Success(true)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun updateUserPhoto(photo: Uri): Resource<Boolean> {
        return  try {
            val uid = currentUser?.uid.toString()
            val storageRef = storage.reference

            val pathImage = "${USER_PATH}photo${uid}.jpg"
            val postImgRef = storageRef.child(pathImage)

            //upload image
            val imageStorageRef = postImgRef.putFile(photo).await()
            val imageURL = imageStorageRef.storage.downloadUrl.await()

            val url = imageURL.toString()

            //User document reference
            val userRef = db.collection(USERS).document(uid)

            userRef.update(PHOTO_URL, url).await()

            Resource.Success(true)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUserByUID(uid: String): Resource<User> {
        return try {
            val userRef = db.collection(USERS).document(uid).get().await()
            val user = userRef.toObject(User::class.java)

            if(user == null){
                Resource.Failure(java.lang.Exception("The document user don't exist"))
            }else{
                Resource.Success(user)
            }
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

}