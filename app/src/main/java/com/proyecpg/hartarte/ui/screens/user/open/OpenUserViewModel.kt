package com.proyecpg.hartarte.ui.screens.user.open

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.proyecpg.hartarte.data.model.toUserUI
import com.proyecpg.hartarte.data.post.PostRepository
import com.proyecpg.hartarte.data.user.UserRepository
import com.proyecpg.hartarte.ui.model.UserUI
import com.proyecpg.hartarte.utils.QueryParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OpenUserViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val postRepository: PostRepository
): ViewModel() {
    private val _newUserState = MutableStateFlow(UserUI())
    val newUserState : StateFlow<UserUI> get() = _newUserState

    val postsUser = postRepository.getPostsBy(query = QueryParams.USER_POST(userRepo.currentUser?.uid.toString())).cachedIn(viewModelScope)

    init{
        viewModelScope.launch {
            getUser()
        }
    }

    fun updateUser(userId: String){
        viewModelScope.launch {
            if(userId.isBlank()){
                return@launch
            }

            val result = userRepo.getUser()

            result.let {
                if(it != null){
                    _newUserState.value = it.toUserUI()
                }
            }
        }
    }

    private suspend fun getUser(){
        _newUserState.value = userRepo.getUser().toUserUI()
    }
}