package com.proyecpg.hartarte.utils

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.proyecpg.hartarte.R

class FirebaseErrors {
    companion object {
        fun handleFirebaseError(e: Exception): Int {
            when (e) {
                is FirebaseAuthInvalidUserException -> {
                    return R.string.error_invalid_credentials
                }
                is FirebaseAuthInvalidCredentialsException -> {
                    return R.string.error_login
                }
                is FirebaseAuthUserCollisionException -> {
                    return R.string.error_in_use_email
                }
            }

            return 0
        }
    }
}