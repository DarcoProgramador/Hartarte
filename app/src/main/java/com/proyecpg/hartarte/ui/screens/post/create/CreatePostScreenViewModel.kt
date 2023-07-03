package com.proyecpg.hartarte.ui.screens.post.create

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecpg.hartarte.R
import com.proyecpg.hartarte.data.post.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostScreenViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    private val _stateCreatePost = MutableStateFlow(CreatePostState())
    val stateCreatePost = _stateCreatePost.asStateFlow()

    fun resetState() {
        _stateCreatePost.update { CreatePostState() }
    }

    fun addPost(images : List<Uri>,
                titulo: String,
                descripcion: String
    ){
        viewModelScope.launch {
            _stateCreatePost.update { it.copy(isLoading = true)}

            if (images.isEmpty()){
                setError(R.string.error_missing_images.toString())
                return@launch
            }

            if (titulo.isBlank()){
                setError(R.string.error_missing_title.toString())
                return@launch
            }

            if (descripcion.isBlank()){
                setError(R.string.error_missing_description.toString())
                return@launch
            }


        }
    }

    private fun setError(error: String){
        _stateCreatePost.update {
            it.copy(
                isLoading = false,
                createPostError = error
            )
        }
    }
}