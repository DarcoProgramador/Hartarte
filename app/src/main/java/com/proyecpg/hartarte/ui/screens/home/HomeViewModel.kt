package com.proyecpg.hartarte.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.google.accompanist.pager.ExperimentalPagerApi
import com.proyecpg.hartarte.data.post.PostRepository
import com.proyecpg.hartarte.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagerApi
class HomeViewModel @Inject constructor(
    private val repo: PostRepository
): ViewModel() {

    val posts = repo.getPosts().cachedIn(viewModelScope)

    fun doLike(postId: String, liked: Boolean){
        viewModelScope.launch {
            val result = repo.registerLike(postId, liked)

            result.let {
                when(val resource = it){
                    is Resource.Success ->{
                        //TODO: Hacer un estado para cuando sea correcto
                    }
                    is Resource.Failure ->{
                        //TODO: Hacer un estado para cuando falle
                    }
                    is Resource.Loading->{
                        //TODO: Hacer un estado para cuando este cargando
                    }
                }
            }
        }
    }

    fun doBookmark(postId: String, bookmarked: Boolean){
        viewModelScope.launch {
            val result = repo.registerBookmark(postId, bookmarked)

            result.let {
                when(val resource = it){
                    is Resource.Success ->{
                        //TODO: Hacer un estado para cuando sea correcto
                    }
                    is Resource.Failure ->{
                        //TODO: Hacer un estado para cuando falle
                    }
                    is Resource.Loading->{
                        //TODO: Hacer un estado para cuando este cargando
                    }
                }
            }
        }
    }
}