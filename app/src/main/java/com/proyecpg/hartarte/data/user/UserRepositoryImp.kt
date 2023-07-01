package com.proyecpg.hartarte.data.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecpg.hartarte.data.model.User
import com.proyecpg.hartarte.utils.Constants.USERS
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

}