package com.nhuhuy.algidy.core.model.setting

enum class ThemeMode {
    SYSTEM, LIGHT, DARK,
}

fun String?.toDarkMode(): ThemeMode {
    return ThemeMode.entries.find { it.name.equals(this, ignoreCase = true) }
        ?: ThemeMode.SYSTEM
}