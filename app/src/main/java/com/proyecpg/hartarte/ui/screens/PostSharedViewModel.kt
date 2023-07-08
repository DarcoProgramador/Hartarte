package com.proyecpg.hartarte.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecpg.hartarte.data.post.PostRepository
import com.proyecpg.hartarte.ui.screens.post.open.OpenPostArgs
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

    var statePost by mutableStateOf(OpenPostArgs())
        private set

    fun onProcess(event : PostSharedEvent){
        when(event){
            is PostSharedEvent.OnLiked -> updateLiked(event.postId, event.like)

            is PostSharedEvent.OnBookmarked -> updateBookmarked(event.postId, event.bookmark)
        }
    }

    private fun updateLiked(postId : String, like : Boolean){
        viewModelScope.launch{
            _stateLiked.value[postId] = like
        }
    }

    private fun updateBookmarked(postId : String, bookmark : Boolean){
        viewModelScope.launch{
            stateBookmarked.value[postId] = bookmark
        }
    }

    fun updatePost(postUI: OpenPostArgs){
        viewModelScope.launch {
            statePost = postUI
        }
    }
}