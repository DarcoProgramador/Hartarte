package com.proyecpg.hartarte.ui.screens.user

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.proyecpg.hartarte.data.model.toUserUI
import com.proyecpg.hartarte.data.post.PostRepository
import com.proyecpg.hartarte.data.user.UserRepository
import com.proyecpg.hartarte.ui.model.UserUI
import com.proyecpg.hartarte.utils.FirebaseErrors
import com.proyecpg.hartarte.utils.QueryParams
import com.proyecpg.hartarte.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val postRepository: PostRepository

): ViewModel() {
    
    private val _userState = MutableStateFlow(UserUI())
    val userState : StateFlow<UserUI> get() = _userState

    private val _editUserState = MutableStateFlow(UserState())
    val editUserState = _editUserState.asStateFlow()

    val postsUser = postRepository.getPostsBy(query = QueryParams.USER_POST(userRepo.currentUser?.uid.toString())).cachedIn(viewModelScope)

    init{
        viewModelScope.launch {
            getUser()
        }
    }

    fun processUser(event: UserEvent){
        when(event){
            is UserEvent.UserEditClicked -> updateUsernameAndDescription(event.username, event.description)

            is UserEvent.UserEditPhotoClicked -> updateUserPhoto(event.photo)

            is UserEvent.UserOnLoadUser -> {
                viewModelScope.launch {
                    getUser()
                }
            }

            is UserEvent.UserPostLikeClicked -> doLike(event.postId, event.liked)

            is UserEvent.UserPostBookmarkCliked -> doBookmark(event.postId, event.bookmarked)
        }
    }
    private suspend fun getUser(){
        _userState.value = userRepo.getUser().toUserUI()
    }

    private fun updateUsernameAndDescription(username : String, descripcion : String){
        viewModelScope.launch {

            _editUserState.update { it.copy(isLoading = true) }

            val result = userRepo.editUser(username, descripcion)

            result.let {
                when(val resource = it){
                    is Resource.Success -> {
                        _editUserState.update {state ->
                            state.copy(
                                isUserEditSuccessful = true,
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Failure -> {
                        val error = FirebaseErrors.handleFirebaseError(resource.exception)

                        if (error != 0){
                            _editUserState.update { state ->
                                state.copy(
                                    userEditError = error.toString(),
                                    isLoading = false
                                )
                            }
                            return@launch
                        }

                        _editUserState.update {state ->
                            state.copy(
                                userEditError = resource.exception.toString(),
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _editUserState.update {state ->
                            state.copy(
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateUserPhoto(photo : Uri){
        viewModelScope.launch {

            _editUserState.update { it.copy(isLoading = true) }

            val result = userRepo.updateUserPhoto(photo)

            result.let {
                when(val resource = it){
                    is Resource.Success -> {
                        _editUserState.update {state ->
                            state.copy(
                                isUserEditSuccessful = true,
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Failure -> {
                        val error = FirebaseErrors.handleFirebaseError(resource.exception)

                        if (error != 0){
                            _editUserState.update { state ->
                                state.copy(
                                    userEditError = error.toString(),
                                    isLoading = false
                                )
                            }
                            return@launch
                        }

                        _editUserState.update {state ->
                            state.copy(
                                userEditError = resource.exception.toString(),
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _editUserState.update {state ->
                            state.copy(
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun doLike(postId: String, liked: Boolean){
        viewModelScope.launch {
            val result = postRepository.registerLike(postId, liked)

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

    private fun doBookmark(postId: String, bookmarked: Boolean){
        viewModelScope.launch {
            val result = postRepository.registerBookmark(postId, bookmarked)

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