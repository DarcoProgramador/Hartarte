package com.proyecpg.hartarte.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.google.accompanist.pager.ExperimentalPagerApi
import com.proyecpg.hartarte.data.post.PostRepository
import com.proyecpg.hartarte.ui.screens.register.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagerApi
class MainViewModel @Inject constructor(
    repo: PostRepository
): ViewModel() {
    val posts = repo.getPosts().cachedIn(viewModelScope)
}