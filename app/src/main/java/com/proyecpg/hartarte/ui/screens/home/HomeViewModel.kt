package com.proyecpg.hartarte.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.google.accompanist.pager.ExperimentalPagerApi
import com.proyecpg.hartarte.data.post.PostRepository
import com.proyecpg.hartarte.utils.QueryParams
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagerApi
class HomeViewModel @Inject constructor(
    private val repo: PostRepository
): ViewModel() {
    val posts = repo.getPostsBy(query = QueryParams.MOST_RECENT).cachedIn(viewModelScope)
}