package com.proyecpg.hartarte.ui.screens.post.open

import androidx.lifecycle.ViewModel
import com.proyecpg.hartarte.data.post.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OpenPostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    
}