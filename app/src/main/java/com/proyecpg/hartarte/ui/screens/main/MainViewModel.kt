package com.proyecpg.hartarte.ui.screens.main

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.google.accompanist.pager.ExperimentalPagerApi
import com.proyecpg.hartarte.data.post.PostRepository
import com.proyecpg.hartarte.domain.GetDarkThemeValue
import com.proyecpg.hartarte.domain.PutDarkThemeValue
import com.proyecpg.hartarte.ui.Event
import com.proyecpg.hartarte.ui.UiState
import com.proyecpg.hartarte.ui.screens.register.RegisterState
import com.proyecpg.hartarte.utils.Constants
import com.proyecpg.hartarte.utils.Constants.DARK_THEME_KEY
import com.proyecpg.hartarte.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagerApi
class MainViewModel @Inject constructor(
    private val repo: PostRepository,
    private val getDarkThemeValue: GetDarkThemeValue,
    private val putDarkThemeValue: PutDarkThemeValue
): ViewModel() {
    val posts = repo.getPosts().cachedIn(viewModelScope)
    var state by mutableStateOf(UiState())
        private set

    init {
        getDarkTheme()
    }

    fun onEvent(e: Event) {1
        when (e) {
            is Event.SaveDarkThemeValue -> saveDarkThemeValue(state.darkThemeValue)
            is Event.SelectedDarkThemeValue -> state = state.copy(darkThemeValue = e.value)
        }
    }

    private fun getDarkTheme() {
        viewModelScope.launch {
            getDarkThemeValue(DARK_THEME_KEY)?.let {
                state = state.copy(darkThemeValue = it)
            }
        }
    }

    private fun saveDarkThemeValue(value: Boolean){
        viewModelScope.launch {
            putDarkThemeValue(DARK_THEME_KEY, value)
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