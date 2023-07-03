package com.proyecpg.hartarte.ui.screens.post.create

import androidx.lifecycle.ViewModel
import com.proyecpg.hartarte.data.post.PostRepository
import com.proyecpg.hartarte.ui.screens.register.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
}