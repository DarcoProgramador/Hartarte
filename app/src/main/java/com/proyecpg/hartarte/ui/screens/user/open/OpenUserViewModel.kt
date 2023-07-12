package com.proyecpg.hartarte.ui.screens.user.open

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.proyecpg.hartarte.data.model.toUserUI
import com.proyecpg.hartarte.data.post.PostRepository
import com.proyecpg.hartarte.data.user.UserRepository
import com.proyecpg.hartarte.domain.model.Post
import com.proyecpg.hartarte.ui.model.UserUI
import com.proyecpg.hartarte.utils.QueryParams
import com.proyecpg.hartarte.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OpenUserViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val postRepository: PostRepository
): ViewModel() {

    private val _userState = MutableStateFlow(UserUI())
    val userState: StateFlow<UserUI> get() = _userState

    private val _postUserState: MutableStateFlow<Flow<PagingData<Post>>?> = MutableStateFlow(null)
    val postUserState get() = _postUserState.asStateFlow()

    init{
        viewModelScope.launch {
            //updateUser(OpenUserState.)
        }
    }

    fun updateUserPosts(userId: String){
        viewModelScope.launch {
            _postUserState.value = postRepository.getPostsBy(query = QueryParams.USER_POST(userId)).cachedIn(viewModelScope)
        }
    }

    fun updateUser(userId: String){
        viewModelScope.launch {
            if(userId.isBlank()){
                return@launch
            }

            val result = userRepo.getUserByUID(userId)

            result.let {
                when(val resource = it){
                    is Resource.Success -> {
                        _userState.value = resource.result!!.toUserUI()
                    }
                    else -> {}
                }
            }
        }
    }
}