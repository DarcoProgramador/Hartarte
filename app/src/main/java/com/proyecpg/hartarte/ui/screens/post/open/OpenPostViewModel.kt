package com.proyecpg.hartarte.ui.screens.post.open

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecpg.hartarte.data.post.PostRepository
import com.proyecpg.hartarte.domain.model.toOpenPostArgs
import com.proyecpg.hartarte.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OpenPostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _statePost = MutableStateFlow(OpenPostArgs())
    val statePost get() = _statePost.asStateFlow()
    
    fun updatePost(postId: String){
        viewModelScope.launch {
            if(postId.isBlank()){
                return@launch
            }
            val result = postRepository.getPostById(postId)

            result.let {
                when(val resource = it){
                    is Resource.Success -> {
                        if(resource.result != null){
                            _statePost.value = resource.result.toOpenPostArgs()
                        }
                    }

                    is Resource.Failure -> {
                        //TODO: Hacer un estado para cuando falle
                    }

                    is Resource.Loading -> {
                        //TODO: Hacer un estado para cuando este cargando
                    }
                }
            }
        }
    }

    fun doLike(postId: String, liked: Boolean){
        viewModelScope.launch {
            val result = postRepository.registerLike(postId, liked)

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
            val result = postRepository.registerBookmark(postId, bookmarked)

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