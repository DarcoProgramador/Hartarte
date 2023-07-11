package com.proyecpg.hartarte.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecpg.hartarte.data.post.PostRepository
import com.proyecpg.hartarte.ui.screens.post.open.OpenPostArgs
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
    fun onProcess(event : PostSharedEvent){
        when(event){
            is PostSharedEvent.OnLiked -> doLike(event.postId, event.like)

            is PostSharedEvent.OnBookmarked -> doBookmark(event.postId, event.bookmark)
        }
    }

    private fun doLike(postId: String, liked: Boolean){
        viewModelScope.launch {
            val result = postRepository.registerLike(postId, liked)

            result.let {
                when(it){
                    is Resource.Success ->{
                        _stateLiked.value[postId] = liked
                    }
                    else -> {
                        //TODO: hacer algo
                    }
                }
            }
        }
    }

    private fun doBookmark(postId: String, bookmarked: Boolean){
        viewModelScope.launch {
            val result = postRepository.registerBookmark(postId, bookmarked)

            result.let {
                when(it){
                    is Resource.Success ->{
                        stateBookmarked.value[postId] = bookmarked
                    }
                    else -> {
                        //TODO: hacer algo
                    }
                }
            }
        }
    }
}