package com.proyecpg.hartarte.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecpg.hartarte.ui.model.PostUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostSharedViewModel @Inject constructor(

) : ViewModel() {

    private val _stateLiked = MutableStateFlow(hashMapOf<String, Boolean>())
    val stateLiked get() = _stateLiked.asStateFlow()

    private val _stateBookmarked = MutableStateFlow(hashMapOf<String, Boolean>())
    val stateBookmarked get() = _stateLiked.asStateFlow()

    var statePost by mutableStateOf(PostUI())
        private set

    fun onProcess(event : PostSharedEvent){
        when(event){
            is PostSharedEvent.onLiked -> updateLiked(event.postId, event.liked)

            is PostSharedEvent.onBookmarked -> updateBookmarked(event.postId, event.bookmarked)
        }
    }

    fun updateLiked(postId : String, isLiked : Boolean){
        viewModelScope.launch{
            _stateLiked.value[postId] = isLiked
        }
    }

    fun updateBookmarked(postId : String, isBookmarked : Boolean){
        viewModelScope.launch{
            _stateBookmarked.value[postId] = isBookmarked
        }
    }

    fun updatePost(postUI: PostUI){
        viewModelScope.launch {
            statePost = postUI
        }
    }
}