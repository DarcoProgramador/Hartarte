package com.proyecpg.hartarte.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

data class SearchState(
    var isMostRecent: Boolean = true,
    var isMostLiked: Boolean = false,
    var isMostBookmarked: Boolean = false
)