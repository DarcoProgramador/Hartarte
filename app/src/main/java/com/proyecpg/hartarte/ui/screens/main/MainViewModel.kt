package com.proyecpg.hartarte.ui.screens.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.proyecpg.hartarte.data.model.toUserUI
import com.proyecpg.hartarte.data.user.UserRepository
import com.proyecpg.hartarte.domain.GetDarkThemeValue
import com.proyecpg.hartarte.domain.PutDarkThemeValue
import com.proyecpg.hartarte.ui.Event
import com.proyecpg.hartarte.ui.UiState
import com.proyecpg.hartarte.ui.model.UserUI
import com.proyecpg.hartarte.utils.Constants.DARK_THEME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagerApi
class MainViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val getDarkThemeValue: GetDarkThemeValue,
    private val putDarkThemeValue: PutDarkThemeValue
): ViewModel() {
    var state by mutableStateOf(UiState())
        private set

    private val _userState = MutableStateFlow(UserUI())
    val userState : StateFlow<UserUI> get() = _userState

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            getDarkTheme()
            getUser()
            _isLoading.emit(false)
        }
    }

    fun onEvent(e: Event) {
        when (e) {
            is Event.SaveDarkThemeValue -> saveDarkThemeValue(state.darkThemeValue)
            is Event.SelectedDarkThemeValue -> state = state.copy(darkThemeValue = e.value)
        }
    }

    private suspend fun getDarkTheme() {
        getDarkThemeValue(DARK_THEME_KEY)?.let {
            state = state.copy(darkThemeValue = it)
        }
    }


    private fun saveDarkThemeValue(value: Boolean){
        viewModelScope.launch {
            putDarkThemeValue(DARK_THEME_KEY, value)
        }
    }

    private suspend fun getUser(){
        _userState.value = userRepo.getUser().toUserUI()
    }
}