package com.proyecpg.hartarte.ui.screens.user

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecpg.hartarte.data.model.toUserUI
import com.proyecpg.hartarte.data.user.UserRepository
import com.proyecpg.hartarte.ui.model.UserUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepo: UserRepository
): ViewModel() {
    
    private val _userState = MutableStateFlow(UserUI())
    val userState : StateFlow<UserUI> get() = _userState

    private val _editUserState = MutableStateFlow(UserState())
    val editUserState = _editUserState.asStateFlow()

    init{
        viewModelScope.launch {
            getUser()
        }
    }

    fun processUser(event: UserEvent){
        when(event){
            is UserEvent.UserEditClicked -> updateUsernameAndDescription(event.username, event.descipcion)

            is UserEvent.UserEditPhotoClicked -> updateUserPhoto(event.photo)

            is UserEvent.UserOnLoadUser -> {
                viewModelScope.launch {
                    getUser()
                }
            }
        }
    }
    private suspend fun getUser(){
        _userState.value = userRepo.getUser().toUserUI()
    }

    private fun updateUsernameAndDescription(username : String, descripcion : String){
        viewModelScope.launch {
            /*TODO: HAcer la actualizacion*/
        }
    }

    private fun updateUserPhoto(photo : Uri){
        viewModelScope.launch {
            /*TODO: HAcer la actualizacion*/
        }
    }
}