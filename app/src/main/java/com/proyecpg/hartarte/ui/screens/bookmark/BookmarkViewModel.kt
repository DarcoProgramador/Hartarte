package com.proyecpg.hartarte.ui.screens.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.accompanist.pager.ExperimentalPagerApi
import com.proyecpg.hartarte.data.post.PostRepository
import com.proyecpg.hartarte.domain.model.Post
import com.proyecpg.hartarte.utils.QueryParams
import com.proyecpg.hartarte.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagerApi
class BookmarkViewModel @Inject constructor(
    private val repo: PostRepository
): ViewModel() {

    private val _postBookmarkState :  MutableStateFlow<Flow<PagingData<Post>>?> = MutableStateFlow(null)
    val postBookmarkState get() = _postBookmarkState.asStateFlow()

    init {
        updateBookmarkedPosts()
    }

    fun updateBookmarkedPosts(){
        viewModelScope.launch {
            val result = repo.getPostBookmarkedQuery()

            result.let{
                when(val resource = it){
                    is Resource.Success -> {
                        _postBookmarkState.value = repo.getPostsBy(query = QueryParams.QUERY_SEARCH(resource.result!!)).cachedIn(viewModelScope)
                    }
                    is Resource.Failure -> {
                        //TODO: Poner estado
                    }
                    is Resource.Loading -> {
                        //TODO: Poner estado
                    }
                }
            }
        }
    }
}