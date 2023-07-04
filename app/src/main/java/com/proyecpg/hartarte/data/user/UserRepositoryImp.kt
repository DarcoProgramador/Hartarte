package com.proyecpg.hartarte.data.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecpg.hartarte.data.model.User
import com.proyecpg.hartarte.ui.model.UserUI
import com.proyecpg.hartarte.utils.Constants.USERS
import com.proyecpg.hartarte.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
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

    override suspend fun editUser(username : String, descripcion : String, photo : String): Resource<Boolean> {
        return  try {
            val uid = currentUser?.uid.toString()

            val userRef = db.collection(USERS).document(uid)

            val userUpdates = mapOf<String, Any>(
                "username" to username,
                "descripcion" to descripcion,
                "photoUrl" to photo
            )

            userRef.update(userUpdates).await()

            Resource.Success(true)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

}