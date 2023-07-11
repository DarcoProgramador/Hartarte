package com.proyecpg.hartarte.ui.screens.post.open

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecpg.hartarte.data.post.PostRepository
import com.proyecpg.hartarte.domain.model.Comment
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

    private val _stateComments = MutableStateFlow(emptyList<Comment>())
    val stateComments = _stateComments.asStateFlow()

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

    fun updateComments(postId: String){
        viewModelScope.launch {
            if(postId.isBlank()){
                return@launch
            }
            val result = postRepository.getComments(postId)
            result.let {
                when(val resource = it){
                    is Resource.Success -> {
                        if(resource.result != null){
                            _stateComments.value = resource.result
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

    fun addComment(postId: String, comment: String){
        viewModelScope.launch {
            if(postId.isBlank() || comment.isBlank()){
                return@launch
            }
            val result = postRepository.registerComment(postId, comment)

            result.let {
                when(val resource = it){
                    is Resource.Success -> {
                        //TODO: Hacer algo cuando el comentario se registre
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
}