package com.proyecpg.hartarte.utils

fun isInteger(s: String): Boolean {
    return try {
        s.toInt()
        true
    } catch (ex: NumberFormatException) {
        false
    }
}