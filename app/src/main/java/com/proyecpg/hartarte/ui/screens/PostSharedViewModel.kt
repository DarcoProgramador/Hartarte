package com.proyecpg.hartarte.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostSharedViewModel @Inject constructor(

) : ViewModel() {

    private val _stateLiked = MutableStateFlow(hashMapOf<String, Boolean>())
    private val stateLiked get() = _stateLiked.asStateFlow()

    private val _stateBookmarked = MutableStateFlow(hashMapOf<String, Boolean>())
    private val stateBookmarked get() = _stateLiked.asStateFlow()


    fun addLiked(postId : String, isLiked : Boolean){
        viewModelScope.launch{
            _stateLiked.value[postId] = isLiked
        }
    }

    fun addBookmarked(postId : String, isBookmarked : Boolean){
        viewModelScope.launch{
            _stateBookmarked.value[postId] = isBookmarked
        }
    }
}