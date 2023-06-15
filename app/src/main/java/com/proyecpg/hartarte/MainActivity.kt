package com.proyecpg.hartarte

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.proyecpg.hartarte.data.DataStoreUtil
import com.proyecpg.hartarte.navigation.NavigationRoot
import com.proyecpg.hartarte.ui.screens.AuthViewModel
import com.proyecpg.hartarte.ui.theme.HartarteTheme
import com.proyecpg.hartarte.ui.theme.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var dataStoreUtil: DataStoreUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataStoreUtil = DataStoreUtil(applicationContext)

        val systemTheme = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> { true }
            Configuration.UI_MODE_NIGHT_NO -> { false }
            else -> { false }
        }

        setContent {
            val theme = dataStoreUtil.getTheme(systemTheme).collectAsState(initial = systemTheme)

            HartarteTheme(
                darkTheme = theme.value
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationRoot(dataStoreUtil = dataStoreUtil, authViewModel = viewModel, themeViewModel = themeViewModel)
                }
            }
        }
        /*setContent {
            HartarteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationRoot(authViewModel = viewModel, )
                }
            }
        }*/
    }
}