package com.proyecpg.hartarte.ui.screens.main

import androidx.lifecycle.ViewModel
import com.proyecpg.hartarte.ui.screens.register.RegisterState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class MainViewModel @Inject constructor(
    //private exampleRepository: ExampleRepository
): ViewModel() {
    private val _stateMain = MutableStateFlow(MainState())
    val stateMain = _stateMain.asStateFlow()

    //No sé xd
    //Mamala, Yender
}