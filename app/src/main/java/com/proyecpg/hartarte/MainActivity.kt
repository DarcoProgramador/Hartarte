package com.proyecpg.hartarte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.proyecpg.hartarte.data.DataStoreUtil
import com.proyecpg.hartarte.navigation.NavigationRoot
import com.proyecpg.hartarte.ui.screens.AuthViewModel
import com.proyecpg.hartarte.ui.screens.main.MainViewModel
import com.proyecpg.hartarte.ui.theme.HartarteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: AuthViewModel by viewModels()
    @OptIn(ExperimentalPagerApi::class)
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var dataStoreUtil: DataStoreUtil

    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value || mainViewModel.isLoading.value
            }
        }

        dataStoreUtil = DataStoreUtil(applicationContext)

        setContent {
            val state = mainViewModel.state

            HartarteTheme(
                darkTheme = state.darkThemeValue
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationRoot(authViewModel = viewModel, mainViewModel = mainViewModel)
                }
            }
        }
    }
}