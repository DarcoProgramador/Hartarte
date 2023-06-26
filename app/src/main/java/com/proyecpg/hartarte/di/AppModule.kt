package com.proyecpg.hartarte.di

import androidx.paging.PagingConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Query.Direction.DESCENDING
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.proyecpg.hartarte.data.paging.PostPagingSource
import com.proyecpg.hartarte.data.post.PostRepository
import com.proyecpg.hartarte.data.post.PostRepositoryImp
import com.proyecpg.hartarte.data.user.UserRepository
import com.proyecpg.hartarte.data.user.UserRepositoryImp
import com.proyecpg.hartarte.utils.Constants.PAGE_SIZE
import com.proyecpg.hartarte.utils.Constants.POST_COLLECTION
import com.proyecpg.hartarte.utils.Constants.TIME_STAMP
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Provides
    fun providequeryPostByCreationTime(): Query = Firebase.firestore
        .collection(POST_COLLECTION)
        .orderBy(TIME_STAMP, DESCENDING)
        .limit(PAGE_SIZE)

    @Provides
    fun provideProductsPagingSource(
        queryPostByCreationTime: Query,
        providerFirebaseAuth : FirebaseAuth,
        providerFirestore: FirebaseFirestore
    ) = PostPagingSource(
        queryPostByCreationTime = queryPostByCreationTime,
        firebaseAuth = providerFirebaseAuth,
        db = providerFirestore
    )

    @Provides
    fun providePagingConfig() = PagingConfig(
        pageSize = PAGE_SIZE.toInt()
    )

    @Provides
    fun providePostsRepository(impl : PostRepositoryImp): PostRepository = impl

    @Provides
    fun provideUserRepository(imp : UserRepositoryImp): UserRepository = imp
}