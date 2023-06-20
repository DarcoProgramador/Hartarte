package com.proyecpg.hartarte.di

import com.proyecpg.hartarte.data.repository.RepositoryImpl
import com.proyecpg.hartarte.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(impl: RepositoryImpl): Repository
}