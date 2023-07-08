package com.proyecpg.hartarte.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecpg.hartarte.data.post.PostRepository
import com.proyecpg.hartarte.ui.model.PostUI
import com.proyecpg.hartarte.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostSharedViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _stateLiked = MutableStateFlow(hashMapOf<String, Boolean>())
    val stateLiked get() = _stateLiked.asStateFlow()

    private val _stateBookmarked = MutableStateFlow(hashMapOf<String, Boolean>())
    val stateBookmarked get() = _stateBookmarked.asStateFlow()

    var statePost by mutableStateOf(PostUI())
        private set

    fun onProcess(event : PostSharedEvent){
        when(event){
            is PostSharedEvent.onLiked -> updateLiked(event.postId)

            is PostSharedEvent.onBookmarked -> updateBookmarked(event.postId)
        }
    }

    private fun updateLiked(postId : String){
        viewModelScope.launch{
            val result = postRepository.getLike(postId)

            result.let {
                when(val resource = it){
                    is Resource.Success -> {
                        _stateLiked.value[postId] = resource.result?:false
                    }
                    is Resource.Failure -> {
                        /*TODO: mostrar error al obtener like*/
                    }
                    is Resource.Loading -> {
                        /*TODO: Hacer algo mientras carga*/
                    }
                }
            }
        }
    }

    private fun updateBookmarked(postId : String){
        viewModelScope.launch{
            val result = postRepository.getBookmark(postId)

            result.let {
                when(val resource = it){
                    is Resource.Success -> {
                        _stateBookmarked.value[postId] = resource.result?:false
                    }
                    is Resource.Failure -> {
                        /*TODO: mostrar error al obtener like*/
                    }
                    is Resource.Loading -> {
                        /*TODO: Hacer algo mientras carga*/
                    }
                }
            }
        }
    }

    fun updatePost(postUI: PostUI){
        viewModelScope.launch {
            statePost = postUI
        }
    }
}