package com.proyecpg.hartarte.domain

import javax.inject.Inject

class GetDarkThemeValue @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(key: String) = repository.getDarkThemeValue(key)
}