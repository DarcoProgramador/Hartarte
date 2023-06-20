package com.proyecpg.hartarte.data.source

interface Preferences {

    suspend fun putDarkThemeValue(key: String, value: Boolean)

    suspend fun getDarkThemeValue(key: String): Boolean?
}