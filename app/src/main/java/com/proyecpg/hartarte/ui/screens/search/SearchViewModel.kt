package com.proyecpg.hartarte.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
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
class SearchViewModel @Inject constructor(
    private val repo: PostRepository
): ViewModel() {

    private val _postSearchState : MutableStateFlow<Flow<PagingData<Post>>?> = MutableStateFlow(null)
    val postSearchState get() = _postSearchState.asStateFlow()

    fun onQueryChange(query: QueryParams?){
        if (query != null){
            _postSearchState.value = repo.getPostsBy(query = query).cachedIn(viewModelScope)
        }
    }

    fun doLike(postId: String, liked: Boolean){
        viewModelScope.launch {
            val result = repo.registerLike(postId, liked)

            result.let {
                when(val resource = it){
                    is Resource.Success ->{
                        //TODO: Hacer un estado para cuando sea correcto
                    }
                    is Resource.Failure ->{
                        //TODO: Hacer un estado para cuando falle
                    }
                    is Resource.Loading->{
                        //TODO: Hacer un estado para cuando este cargando
                    }
                }
            }
        }
    }

    fun doBookmark(postId: String, bookmarked: Boolean){
        viewModelScope.launch {
            val result = repo.registerBookmark(postId, bookmarked)

            result.let {
                when(val resource = it){
                    is Resource.Success ->{
                        //TODO: Hacer un estado para cuando sea correcto
                    }
                    is Resource.Failure ->{
                        //TODO: Hacer un estado para cuando falle
                    }
                    is Resource.Loading->{
                        //TODO: Hacer un estado para cuando este cargando
                    }
                }
            }
        }
    }
}