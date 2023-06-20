package com.proyecpg.hartarte.ui

sealed class Event{
    data class SaveDarkThemeValue(val value: Boolean): Event()
    data class SelectedDarkThemeValue(val value: Boolean): Event()
}
