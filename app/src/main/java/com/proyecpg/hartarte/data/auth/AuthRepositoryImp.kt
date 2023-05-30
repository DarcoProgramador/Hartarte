package com.proyecpg.hartarte.data.auth

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.proyecpg.hartarte.utils.Constants.SIGN_IN_REQUEST
import com.proyecpg.hartarte.utils.Constants.SIGN_UP_REQUEST
import com.proyecpg.hartarte.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class AuthRepositoryImp @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,
    ): AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            return Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun singInWithCredentials(credentials: AuthCredential): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithCredential(credentials).await()
            val isNewUser = result.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                TODO("implement funtion addUserToFirestore() to add more data for the users")
            }
            return Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun oneTapSignInWithGoogle(): Resource<BeginSignInResult> {
        return try {
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            return Resource.Success(signInResult)
        } catch (e: Exception) {
            try {
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                Resource.Success(signUpResult)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure(e)
            }
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}