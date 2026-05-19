package com.nhuhuy.algidy.core.model.setting

enum class DarkMode {
    DARK, SYSTEM, LIGHT
}

fun String?.toDarkMode(): DarkMode {
    return DarkMode.entries.find { it.name.equals(this, ignoreCase = true) }
        ?: DarkMode.SYSTEM
}