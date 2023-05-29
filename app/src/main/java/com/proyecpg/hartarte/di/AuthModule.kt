package com.proyecpg.hartarte.di

import com.google.firebase.auth.FirebaseAuth
import com.proyecpg.hartarte.data.auth.AuthRepository
import com.proyecpg.hartarte.data.auth.AuthRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AuthModule {
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun providesAuthRepository(impl: AuthRepositoryImp): AuthRepository = impl

}