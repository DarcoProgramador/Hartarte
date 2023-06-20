package com.proyecpg.hartarte.domain

interface Repository {

    suspend fun putDarkThemeValue(key: String, value: Boolean)

    suspend fun getDarkThemeValue(key: String): Boolean?
}