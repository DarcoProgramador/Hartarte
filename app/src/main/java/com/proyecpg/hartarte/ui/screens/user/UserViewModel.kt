package com.proyecpg.hartarte.ui.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecpg.hartarte.data.model.toUserUI
import com.proyecpg.hartarte.data.user.UserRepository
import com.proyecpg.hartarte.ui.model.UserUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepo: UserRepository
): ViewModel() {
    
    private val _userState = MutableStateFlow(UserUI())
    val userState : StateFlow<UserUI> get() = _userState

    init{
        viewModelScope.launch {
            getUser()
        }
    }

    fun processUser(event: UserEvent){
        when(event){
            is UserEvent.UserEditClicked -> {/*TODO: Implementar la funcion para editar descipcion y username*/}

            is UserEvent.UserEditPhotoClicked -> {/*TODO Implementar la funcion para editar la foto de perfil*/}

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
}