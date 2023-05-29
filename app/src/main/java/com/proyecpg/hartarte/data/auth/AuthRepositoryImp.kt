package com.proyecpg.hartarte.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.proyecpg.hartarte.vo.Resource

class AuthRepositoryImp(
    private val firebaseAuth: FirebaseAuth,

    ): AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        TODO("Añadir la funcion para loguearme")
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String
    ): Resource<FirebaseUser> {
        TODO("Añadir la funcion para registrarme")
    }

    override fun logout() {
        TODO("Añadir la funcion para desloguearme")
    }
}