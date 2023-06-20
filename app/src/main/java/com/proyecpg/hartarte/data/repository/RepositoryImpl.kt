package com.proyecpg.hartarte.data.repository

import com.proyecpg.hartarte.data.source.Preferences
import com.proyecpg.hartarte.domain.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val preferences: Preferences
): Repository {

    override suspend fun putDarkThemeValue(key: String, value: Boolean) {
        preferences.putDarkThemeValue(key, value)
    }

    override suspend fun getDarkThemeValue(key: String): Boolean? {
        return preferences.getDarkThemeValue(key)
    }
}